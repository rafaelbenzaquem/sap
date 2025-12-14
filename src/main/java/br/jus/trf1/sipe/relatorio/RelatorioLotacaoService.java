package br.jus.trf1.sipe.relatorio;

import br.jus.trf1.sipe.arquivo.domain.port.in.ArquivoServicePort;
import br.jus.trf1.sipe.feriado.externo.jsarh.FeriadoJSarhClient;
import br.jus.trf1.sipe.feriado.externo.jsarh.dto.FeriadoJSarhResponse;
import br.jus.trf1.sipe.ponto.domain.service.PontoServiceAdapter;
import br.jus.trf1.sipe.relatorio.model.UsuarioModel;
import br.jus.trf1.sipe.servidor.domain.service.ServidorServiceAdapter;
import br.jus.trf1.sipe.usuario.infrastructure.security.UsuarioSecurityAdapter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static br.jus.trf1.sipe.comum.util.ConstParamUtil.*;
import static br.jus.trf1.sipe.relatorio.RelatorioUtil.mapeandoParametrosRelatorio;
import static br.jus.trf1.sipe.relatorio.RelatorioUtil.processaDadosServidorParaRelatorio;
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
    private final PontoServiceAdapter pontoServiceAdapter;
    private final ArquivoServicePort arquivoServicePort;
    private final ServidorServiceAdapter servidorService;
    private final UsuarioSecurityAdapter usuarioSecurityAdapter;


    /**
     * Constrói o serviço de relatório com as dependências necessárias.
     *
     * @param feriadoExternalClient Repositório de vínculos.
     * @param pontoServiceAdapter          Serviço que contrala pontos.
     * @param arquivoServicePort     Repositório de arquivos.
     * @param servidorService       Serviço de acesso a dados do Servidor no Sarh
     */
    public RelatorioLotacaoService(FeriadoJSarhClient feriadoExternalClient, PontoServiceAdapter pontoServiceAdapter,
                                   ArquivoServicePort arquivoServicePort, ServidorServiceAdapter servidorService,
                                   UsuarioSecurityAdapter usuarioSecurityAdapter) {
        this.feriadoExternalClient = feriadoExternalClient;
        this.pontoServiceAdapter = pontoServiceAdapter;
        this.arquivoServicePort = arquivoServicePort;
        this.servidorService = servidorService;
        this.usuarioSecurityAdapter = usuarioSecurityAdapter;
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
        usuarioSecurityAdapter.permissoesNivelUsuario(matricula);
        log.info("Carregando pontos para o período especificado...");

        var servidorPrincipal = servidorService.atualizaDadosDoSarh(matricula);

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
                    servidorService.atualizaDadosDoSarh(matriculaSubordinado);

            log.info("Consultando ausenciais(licenças, férias e ausências especiais) do servidor no SARH...");
            subordinado = servidorService.vinculaAusenciasServidorNoPeriodo(subordinado, inicio, fim);
            log.info("Ausencias: {}", subordinado.getAusencias().size());

            log.info("Carregando pontos para o período especificado...");
            var pontos = pontoServiceAdapter.carregaPontos(matriculaSubordinado, inicio, fim);
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
        var pontos = pontoServiceAdapter.carregaPontos(matricula, inicio, fim);
        log.info("Total de pontos recuperados: {}", pontos.size());

        var relatorioModel = processaDadosServidorParaRelatorio(servidorPrincipal, pontos, feriados);

        log.info("Carregando arquivos do relatório...");
        var arquivoLogoImagemEsquerdaSuperior = arquivoServicePort.recuperaPorNome(LOGO_SUPERIOR_ESQUERDO);
        var arquivoLogoImagemDireitaSuperior = arquivoServicePort.recuperaPorNome(LOGO_SUPERIOR_DIREITO);
        var arquivoRelatorioPonto = arquivoServicePort.recuperaPorNome(TEMPLATE_RELATORIO_LOTACAO);

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
