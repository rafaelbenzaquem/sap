package br.jus.trf1.sipe.relatorio;

import br.jus.trf1.sipe.arquivo.db.ArquivoRepository;
import br.jus.trf1.sipe.feriado.externo.jsarh.FeriadoJSarhClient;
import br.jus.trf1.sipe.feriado.externo.jsarh.dto.FeriadoJSarhResponse;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.relatorio.model.UsuarioModel;
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
import java.util.List;

import static br.jus.trf1.sipe.relatorio.RelatorioUtil.mapeandoParametrosRelatorio;
import static br.jus.trf1.sipe.relatorio.RelatorioUtil.processaDadosServidorParaRelatorio;
import static br.jus.trf1.sipe.comum.util.ConstParamUtil.*;
import static net.sf.jasperreports.engine.JasperExportManager.exportReportToPdf;
import static net.sf.jasperreports.engine.JasperFillManager.fillReport;

/**
 * Serviço responsável por gerar relatórios de pontos.
 * Utiliza JasperReports para criar relatórios em PDF com base nos dados fornecidos.
 */
@Service
@Slf4j
public class RelatorioLotacaoService implements RelatorioService {


    private final FeriadoJSarhClient feriadoExternalClient;
    private final PontoService pontoService;
    private final ArquivoRepository arquivoRepository;
    private final ServidorService servidorService;
    private final UsuarioAtualService usuarioAtualService;


    /**
     * Constrói o serviço de relatório com as dependências necessárias.
     *
     * @param feriadoExternalClient Repositório de vínculos.
     * @param pontoService          Serviço que contrala pontos.
     * @param arquivoRepository     Repositório de arquivos.
     * @param servidorService       Serviço de acesso a dados do Servidor no Sarh
     */
    public RelatorioLotacaoService(FeriadoJSarhClient feriadoExternalClient, PontoService pontoService,
                                   ArquivoRepository arquivoRepository, ServidorService servidorService,
                                   UsuarioAtualService usuarioAtualService) {
        this.feriadoExternalClient = feriadoExternalClient;
        this.pontoService = pontoService;
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

        var servidorPrincipal = servidorService.atualizaDadosNoSarh(matricula);

        var subordinados = servidorService.listar(servidorPrincipal.getLotacao().getId());

        List<UsuarioModel> subordinadosModel = new ArrayList<>();

        log.info("Consultando feriados no SARH...");
        var feriados = feriadoExternalClient.buscaFeriados(inicio, fim, null).
                stream().map(FeriadoJSarhResponse::toModel).toList();
        log.info("Feriados: {}", feriados.size());

        for (var subordinado : subordinados) {
            var matriculaSubordinado = subordinado.getMatricula();

            log.info("Vinculando usuário com seus dados do SARH...");
            subordinado = (subordinado.equals(servidorPrincipal)) ? servidorPrincipal :
                    servidorService.atualizaDadosNoSarh(matriculaSubordinado);

            log.info("Consultando ausenciais(licenças, férias e ausências especiais) do servidor no SARH...");
            subordinado = servidorService.vinculaAusenciasServidorNoPeriodo(subordinado, inicio, fim);
            log.info("Ausencias: {}", subordinado.getAusencias().size());

            log.info("Carregando pontos para o período especificado...");
            var pontos = pontoService.carregaPontos(matriculaSubordinado, inicio, fim);
            log.info("Total de pontos recuperados: {}", pontos.size());

            var relatorioModel = processaDadosServidorParaRelatorio(subordinado, pontos, feriados);

            var subordinadoModel = relatorioModel.getUsuario();

            subordinadoModel.setDescricao(
                    "Horas a serem cumpridas: " + relatorioModel.getTextoHorasUteis() + "\n" +
                            "Total de horas trabalhadas: " + relatorioModel.getTextoPermanenciaTotal() + "\n" +
                            relatorioModel.getRotuloHorasCreditoOuDebito() + ":" +
                            relatorioModel.getTextoHorasCreditoOuDebito());

            subordinadosModel.add(subordinadoModel);
        }

        log.info("Carregando pontos para o período especificado...");
        var pontos = pontoService.carregaPontos(matricula, inicio, fim);
        log.info("Total de pontos recuperados: {}", pontos.size());

        var relatorioModel = processaDadosServidorParaRelatorio(servidorPrincipal, pontos, feriados);

        log.info("Carregando arquivos do relatório...");
        var arquivoLogoImagemEsquerdaSuperior = arquivoRepository.findByNome(LOGO_SUPERIOR_ESQUERDO).
                orElseThrow(() -> new IllegalArgumentException(ATTRIB_NAO_ENCONTRADO.formatted(LOGO_SUPERIOR_ESQUERDO)));
        var arquivoLogoImagemDireitaSuperior = arquivoRepository.findByNome(LOGO_SUPERIOR_DIREITO).
                orElseThrow(() -> new IllegalArgumentException(ATTRIB_NAO_ENCONTRADO.formatted(LOGO_SUPERIOR_DIREITO)));
        var arquivoRelatorioPonto = arquivoRepository.findByNome(TEMPLATE_RELATORIO_LOTACAO).
                orElseThrow(() -> new IllegalArgumentException(ATTRIB_NAO_ENCONTRADO.formatted(TEMPLATE_RELATORIO_LOTACAO)));

        log.info("Mapeando parâmetros para o relatório...");
        var parametrosRelatorio = mapeandoParametrosRelatorio(relatorioModel,
                arquivoLogoImagemEsquerdaSuperior,
                arquivoLogoImagemDireitaSuperior,
                inicio, fim,
                new JRBeanCollectionDataSource(subordinadosModel, false));

        var streamRelatorioPonto = new ByteArrayInputStream(arquivoRelatorioPonto.getBytes());
        var printRelatorioPonto = fillReport(streamRelatorioPonto, parametrosRelatorio, new JREmptyDataSource());
        return exportReportToPdf(printRelatorioPonto);
    }

}
