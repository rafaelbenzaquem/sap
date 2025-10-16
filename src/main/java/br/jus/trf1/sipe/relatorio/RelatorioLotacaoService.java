package br.jus.trf1.sipe.relatorio;

import br.jus.trf1.sipe.arquivo.db.Arquivo;
import br.jus.trf1.sipe.arquivo.db.ArquivoRepository;
import br.jus.trf1.sipe.externo.jsarh.feriado.FeriadoExternal;
import br.jus.trf1.sipe.externo.jsarh.feriado.FeriadoExternalClient;
import br.jus.trf1.sipe.externo.jsarh.feriado.dto.FeriadoExternalResponse;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.PontoRepository;
import br.jus.trf1.sipe.relatorio.model.RelatorioLotacaoData;
import br.jus.trf1.sipe.relatorio.model.RelatorioPontoData;
import br.jus.trf1.sipe.relatorio.model.UsuarioRelatorioLotacaoModel;
import br.jus.trf1.sipe.relatorio.model.UsuarioRelatorioPontoModel;
import br.jus.trf1.sipe.servidor.Servidor;
import br.jus.trf1.sipe.servidor.ServidorService;
import br.jus.trf1.sipe.usuario.UsuarioAtualService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static br.jus.trf1.sipe.relatorio.model.util.FomatadorTextoUtil.formataTextoPeriodo;
import static net.sf.jasperreports.engine.JasperExportManager.exportReportToPdf;
import static net.sf.jasperreports.engine.JasperFillManager.fillReport;

/**
 * Serviço responsável por gerar relatórios de pontos.
 * Utiliza JasperReports para criar relatórios em PDF com base nos dados fornecidos.
 */
@Service
@Slf4j
public class RelatorioLotacaoService implements RelatorioService {


    private final FeriadoExternalClient feriadoExternalClient;
    private final PontoRepository pontoRepository;
    private final ArquivoRepository arquivoRepository;
    private final ServidorService servidorService;
    private final UsuarioAtualService usuarioAtualService;


    /**
     * Constrói o serviço de relatório com as dependências necessárias.
     *
     * @param feriadoExternalClient Repositório de vínculos.
     * @param pontoRepository       Repositório de pontos.
     * @param arquivoRepository     Repositório de arquivos.
     * @param servidorService       Serviço de acesso a dados do Servidor no Sarh
     */
    public RelatorioLotacaoService(FeriadoExternalClient feriadoExternalClient, PontoRepository pontoRepository,
                                   ArquivoRepository arquivoRepository, ServidorService servidorService,
                                   UsuarioAtualService usuarioAtualService) {
        this.feriadoExternalClient = feriadoExternalClient;
        this.pontoRepository = pontoRepository;
        this.arquivoRepository = arquivoRepository;
        this.servidorService = servidorService;
        this.usuarioAtualService = usuarioAtualService;
    }


    /**
     * Gera um relatório em PDF com a lista de servidores e seus dados de ponto de um setor.
     *
     * @param matricula Matrícula do usuário.
     * @param inicio    Data de início do período.
     * @param fim       Data de fim do período.
     * @return Relatório em formato de array de bytes (PDF).
     * @throws JRException Se ocorrer um erro ao gerar o relatório.
     */
    public byte[] gerarRelatorio(String matricula, LocalDate inicio, LocalDate fim) throws JRException {
        log.info("Iniciando geração de relatório para matrícula: {}, período: {} a {}", matricula, inicio, fim);
        usuarioAtualService.permissoesNivelUsuario(matricula);
        log.info("Carregando pontos para o período especificado...");

        var servidorPrincipal = servidorService.vinculaUsuarioServidor(matricula);

        var subordinados = servidorService.listar(servidorPrincipal.getLotacao().getId());

        List<UsuarioRelatorioLotacaoModel> subordinadosModel = new ArrayList<>();

        log.info("Consultando feriados no SARH...");
        var feriados = feriadoExternalClient.buscaFeriados(inicio, fim, null).
                stream().map(FeriadoExternalResponse::toModel).toList();
        log.info("Feriados: {}", feriados.size());

        for (var subordinado : subordinados) {
            var matriculaSubordinado = subordinado.getMatricula();
            log.info("Carregando pontos para o período especificado...");
            var pontos = pontoRepository.buscaPontosPorPeriodo(matriculaSubordinado, inicio, fim);
            log.info("Total de pontos recuperados: {}", pontos.size());

            log.info("Vinculando usuário com seus dados do SARH...");
            subordinado = servidorService.vinculaUsuarioServidor(matriculaSubordinado);

            log.info("Consultando ausenciais(licenças, férias e ausências especiais) do servidor no SARH...");
            subordinado = servidorService.vinculaAusenciasServidorNoPeriodo(subordinado, inicio, fim);
            log.info("Ausencias: {}", subordinado.getAusencias().size());

            log.info("Construindo modelo de usuário...");
            var subordinadoModel = UsuarioRelatorioLotacaoModel.builder()
                    .nome(subordinado.getNome())
                    .lotacao(subordinado.getLotacao().getDescricao())
                    .matricula(matriculaSubordinado)
                    .horasDiaria(subordinado.getHoraDiaria())
                    .ausencias(subordinado.getAusencias())
                    .build();
            log.info("Construindo modelo de relatório...");

            var relatorioLotacaoModel = new RelatorioLotacaoData(subordinadoModel, pontos, feriados);

            subordinadoModel.setDescricao(
                    "Horas a serem cumpridas: " + relatorioLotacaoModel.getTextoHorasUteis() + "\t" +
                            "Total de horas trabalhadas: " + relatorioLotacaoModel.getTextoPermanenciaTotal() + "\t" +
                            relatorioLotacaoModel.getRotuloHorasCreditoOuDebito() + ":" +
                            relatorioLotacaoModel.getTextoHorasCreditoOuDebito());

            subordinadosModel.add(subordinadoModel);
        }


        log.info("Preparando parâmetros para o relatório...");
        log.info("Carregando imagens e arquivo de relatório...");
        var logoImagem = arquivoRepository.findByNome("logoImagem.png").
                orElseThrow(() -> new IllegalArgumentException("Arquivo 'logoImagem.png' não encontrado"));
        var logoImagem2 = arquivoRepository.findByNome("logoImagem2.png").
                orElseThrow(() -> new IllegalArgumentException("Arquivo 'logoImagem2.png' não encontrado"));
        var arquivoRelatorioPonto = arquivoRepository.findByNome("relatorioLotacaoA4.jasper").
                orElseThrow(() -> new IllegalArgumentException("Arquivo 'relatorioALotacao4.jasper' não encontrado"));

        var parametrosRelatorio = new HashMap<String, Object>();

        parametrosRelatorio.put("logoImagem", logoImagem.getBytes());
        parametrosRelatorio.put("logoImagem2", logoImagem2.getBytes());
        parametrosRelatorio.put("obs", "Sem observações...");
        parametrosRelatorio.put("totalHorasUteis", "Teste 1");
        parametrosRelatorio.put("totalHorasPermanencia", "Teste 2");
        parametrosRelatorio.put("creditoDebitoLabel", "Teste 3");
        parametrosRelatorio.put("horaCDTotal", "Teste 4");

        parametrosRelatorio.put("pontosDataSource", new JRBeanCollectionDataSource(subordinadosModel, false));

        parametrosRelatorio.put("nome", servidorPrincipal.getNome());
        parametrosRelatorio.put("matricula", servidorPrincipal.getMatricula());
        parametrosRelatorio.put("lotacao", servidorPrincipal.getLotacao().getDescricao() + "(" + servidorPrincipal.getLotacao().getSigla() + ")");
        parametrosRelatorio.put("periodo", formataTextoPeriodo(inicio, fim));

        var streamRelatorioPonto = new ByteArrayInputStream(arquivoRelatorioPonto.getBytes());
        var printRelatorioPonto = fillReport(streamRelatorioPonto, parametrosRelatorio, new JREmptyDataSource());
        return exportReportToPdf(printRelatorioPonto);
    }

}
