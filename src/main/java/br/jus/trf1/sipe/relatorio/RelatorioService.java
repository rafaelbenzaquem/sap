package br.jus.trf1.sipe.relatorio;

import br.jus.trf1.sipe.arquivo.db.ArquivoRepository;
import br.jus.trf1.sipe.externo.jsarh.feriado.FeriadoExternalClient;
import br.jus.trf1.sipe.externo.jsarh.feriado.dto.FeriadoExternalResponse;
import br.jus.trf1.sipe.ponto.PontoRepository;
import br.jus.trf1.sipe.relatorio.model.RelatorioLotacaoData;
import br.jus.trf1.sipe.relatorio.model.RelatorioPontoData;
import br.jus.trf1.sipe.relatorio.model.UsuarioRelatorioLotacaoModel;
import br.jus.trf1.sipe.relatorio.model.UsuarioRelatorioPontoModel;
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
@Slf4j
@Service
public class RelatorioService {


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
    public RelatorioService(FeriadoExternalClient feriadoExternalClient, PontoRepository pontoRepository,
                            ArquivoRepository arquivoRepository, ServidorService servidorService,
                            UsuarioAtualService usuarioAtualService) {
        this.feriadoExternalClient = feriadoExternalClient;
        this.pontoRepository = pontoRepository;
        this.arquivoRepository = arquivoRepository;
        this.servidorService = servidorService;
        this.usuarioAtualService = usuarioAtualService;
    }

    /**
     * Gera um relatório em PDF com os pontos registrados para um usuário em um período específico.
     *
     * @param matricula Matrícula do reponsável pelo setor.
     * @param inicio    Data de início do período.
     * @param fim       Data de fim do período.
     * @return Relatório em formato de array de bytes (PDF).
     * @throws JRException Se ocorrer um erro ao gerar o relatório.
     */
    public byte[] gerarRelatorioPorMatriculaUsuario(String matricula, LocalDate inicio, LocalDate fim) throws JRException {
        log.info("Iniciando geração de relatório para matrícula: {}, período: {} a {}", matricula, inicio, fim);
        usuarioAtualService.permissoesNivelUsuario(matricula);
        log.info("Carregando pontos para o período especificado...");
        var pontos = pontoRepository.buscaPontosPorPeriodo(matricula, inicio, fim);
        log.info("Total de pontos recuperados: {}", pontos.size());


        var servidor = servidorService.vinculaUsuarioServidor(matricula);

        log.info("Consultando feriados no SARH...");
        var feriados = feriadoExternalClient.buscaFeriados(inicio, fim, null).
                stream().map(FeriadoExternalResponse::toModel).toList();

        log.info("Consultando licenças, férias e ausências especiais do servidor no SARH...");
        servidor = servidorService.vinculaAusenciasServidorNoPeriodo(servidor, inicio, fim);

        log.info("Ausencias: {}", servidor.getAusencias().size());

        log.info("Feriados: {}", feriados.size());

        log.info("Construindo modelo de usuário...");
        var usuario = UsuarioRelatorioPontoModel.builder()
                .nome(servidor.getNome())
                .cargo(servidor.getCargo() == null ? "Servidor Requisitado" : servidor.getCargo())
                .funcao(servidor.getFuncao())
                .lotacao(servidor.getLotacao().getDescricao())
                .matricula(matricula)
                .horasDiaria(servidor.getHoraDiaria())
                .ausencias(servidor.getAusencias())
                .build();
        log.info("Construindo modelo de relatório...");

        var relatorioModel = new RelatorioPontoData(usuario, pontos, feriados);

        log.info("Preparando parâmetros para o relatório...");
        log.info("Carregando imagens e arquivo de relatório...");
        var logoImagem = arquivoRepository.findByNome("logoImagem.png").
                orElseThrow(() -> new IllegalArgumentException("Arquivo 'logoImagem.png' não encontrado"));
        var logoImagem2 = arquivoRepository.findByNome("logoImagem2.png").
                orElseThrow(() -> new IllegalArgumentException("Arquivo 'logoImagem2.png' não encontrado"));
        var arquivoRelatorioPonto = arquivoRepository.findByNome("relatorioA4.jasper").
                orElseThrow(() -> new IllegalArgumentException("Arquivo 'relatorioA4.jasper' não encontrado"));

        var parametrosRelatorio = new HashMap<String, Object>();

        parametrosRelatorio.put("logoImagem", logoImagem.getBytes());
        parametrosRelatorio.put("logoImagem2", logoImagem2.getBytes());
        parametrosRelatorio.put("obs", "Sem observações...");
        parametrosRelatorio.put("totalHorasUteis", relatorioModel.getTextoHorasUteis());
        parametrosRelatorio.put("totalHorasPermanencia", relatorioModel.getTextoPermanenciaTotal());
        parametrosRelatorio.put("creditoDebitoLabel", relatorioModel.getRotuloHorasCreditoOuDebito());
        parametrosRelatorio.put("horaCDTotal", relatorioModel.getTextoHorasCreditoOuDebito());

        parametrosRelatorio.put("pontosDataSource", relatorioModel.getPontosDataSource());

        parametrosRelatorio.put("nome", usuario.nome());
        parametrosRelatorio.put("cargo", usuario.cargo());
        parametrosRelatorio.put("funcao", usuario.funcao());
        parametrosRelatorio.put("matricula", usuario.matricula());
        parametrosRelatorio.put("lotacao", usuario.lotacao());
        parametrosRelatorio.put("periodo", formataTextoPeriodo(inicio, fim));

        var streamRelatorioPonto = new ByteArrayInputStream(arquivoRelatorioPonto.getBytes());
        var printRelatorioPonto = fillReport(streamRelatorioPonto, parametrosRelatorio, new JREmptyDataSource());
        return exportReportToPdf(printRelatorioPonto);
    }


    /**
     * Gera um relatório em PDF com os pontos registrados para um usuário em um período específico.
     *
     * @param matricula Matrícula do usuário.
     * @param inicio    Data de início do período.
     * @param fim       Data de fim do período.
     * @return Relatório em formato de array de bytes (PDF).
     * @throws JRException Se ocorrer um erro ao gerar o relatório.
     */
    public byte[] gerarRelatorioPorLotacao(String matricula, LocalDate inicio, LocalDate fim) throws JRException {
        log.info("Iniciando geração de relatório para matrícula: {}, período: {} a {}", matricula, inicio, fim);
        usuarioAtualService.permissoesNivelUsuario(matricula);
        log.info("Carregando pontos para o período especificado...");

        var servidorPrincipal = servidorService.vinculaUsuarioServidor(matricula);

        var subordinados = servidorService.listar(servidorPrincipal.getLotacao().getId());

        List<UsuarioRelatorioLotacaoModel> subordinadosModel = new ArrayList<>();

        for (var subordinado : subordinados) {
            var matriculaSubordinado = subordinado.getMatricula();
            var pontos = pontoRepository.buscaPontosPorPeriodo(matriculaSubordinado, inicio, fim);
            log.info("Total de pontos recuperados: {}", pontos.size());


            log.info("Consultando feriados no SARH...");
            var feriados = feriadoExternalClient.buscaFeriados(inicio, fim, null).
                    stream().map(FeriadoExternalResponse::toModel).toList();

            log.info("Consultando licenças, férias e ausências especiais do servidor no SARH...");
            subordinado = servidorService.vinculaAusenciasServidorNoPeriodo(subordinado, inicio, fim);

            log.info("Ausencias: {}", subordinado.getAusencias().size());

            log.info("Feriados: {}", feriados.size());

            log.info("Construindo modelo de usuário...");
            var usuario = UsuarioRelatorioPontoModel.builder()
                    .nome(subordinado.getNome())
                    .cargo(subordinado.getCargo() == null ? "Servidor Requisitado" : subordinado.getCargo())
                    .funcao(subordinado.getFuncao())
                    .lotacao(subordinado.getLotacao().getDescricao())
                    .matricula(matriculaSubordinado)
                    .horasDiaria(subordinado.getHoraDiaria())
                    .ausencias(subordinado.getAusencias())
                    .build();
            log.info("Construindo modelo de relatório...");

            var relatorioLotacaoModel = new RelatorioLotacaoData(usuario, pontos, feriados);

            var subordinadoModel = new UsuarioRelatorioLotacaoModel(usuario.nome(),usuario.matricula(),
                    "totalHorasUteis: " + relatorioLotacaoModel.getTextoHorasUteis() + "\t" +
                            "totalHorasPermanencia: " + relatorioLotacaoModel.getTextoPermanenciaTotal() + "\t" +
                            "creditoDebitoLabel: " + relatorioLotacaoModel.getRotuloHorasCreditoOuDebito() + "\t" +
                            "horaCDTotal: " + relatorioLotacaoModel.getTextoHorasCreditoOuDebito());

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
        parametrosRelatorio.put("totalHorasPermanencia","Teste 2");
        parametrosRelatorio.put("creditoDebitoLabel", "Teste 3");
        parametrosRelatorio.put("horaCDTotal", "Teste 4");

        parametrosRelatorio.put("pontosDataSource", new JRBeanCollectionDataSource(subordinadosModel, false));

        parametrosRelatorio.put("nome", servidorPrincipal.getNome());
        parametrosRelatorio.put("cargo", servidorPrincipal.getCargo());
        parametrosRelatorio.put("funcao", servidorPrincipal.getFuncao());
        parametrosRelatorio.put("matricula", servidorPrincipal.getMatricula());
        parametrosRelatorio.put("lotacao", servidorPrincipal.getLotacao().getDescricao());
        parametrosRelatorio.put("periodo", formataTextoPeriodo(inicio, fim));

        var streamRelatorioPonto = new ByteArrayInputStream(arquivoRelatorioPonto.getBytes());
        var printRelatorioPonto = fillReport(streamRelatorioPonto, parametrosRelatorio, new JREmptyDataSource());
        return exportReportToPdf(printRelatorioPonto);
    }

}
