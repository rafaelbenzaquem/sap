package br.jus.trf1.sipe.relatorio;

import br.jus.trf1.sipe.arquivo.db.ArquivoRepository;
import br.jus.trf1.sipe.feriado.externo.jsarh.FeriadoJSarhClient;
import br.jus.trf1.sipe.feriado.externo.jsarh.dto.FeriadoJSarhResponse;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.servidor.domain.service.ServidorService;
import br.jus.trf1.sipe.usuario.infrastructure.security.UsuarioSecurityAdapter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.ConstParamUtil.*;
import static net.sf.jasperreports.engine.JasperExportManager.exportReportToPdf;
import static net.sf.jasperreports.engine.JasperFillManager.fillReport;
import static br.jus.trf1.sipe.relatorio.RelatorioUtil.*;

/**
 * Serviço responsável por gerar relatórios de pontos.
 * Utiliza JasperReports para criar relatórios em PDF com base nos dados fornecidos.
 */
@Slf4j
@Service
public class RelatorioUsuarioService implements RelatorioService {


    private final FeriadoJSarhClient feriadoExternalClient;
    private final PontoService pontoService;
    private final ArquivoRepository arquivoRepository;
    private final ServidorService servidorService;
    private final UsuarioSecurityAdapter usuarioSecurityAdapter;


    /**
     * Constrói o serviço de relatório com as dependências necessárias.
     *
     * @param feriadoExternalClient Repositório de vínculos.
     * @param pontoService          Serviço que controla pontos.
     * @param arquivoRepository     Repositório de arquivos.
     * @param servidorService       Serviço de acesso a dados do Servidor no Sarh
     */
    public RelatorioUsuarioService(FeriadoJSarhClient feriadoExternalClient, PontoService pontoService,
                                   ArquivoRepository arquivoRepository, ServidorService servidorService,
                                   UsuarioSecurityAdapter usuarioSecurityAdapter) {
        this.feriadoExternalClient = feriadoExternalClient;
        this.pontoService = pontoService;
        this.arquivoRepository = arquivoRepository;
        this.servidorService = servidorService;
        this.usuarioSecurityAdapter = usuarioSecurityAdapter;
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
    public byte[] gerarRelatorio(String matricula, LocalDate inicio, LocalDate fim) throws JRException {

        log.info("Iniciando geração de relatório para matrícula: {}, período: {} a {}", matricula, inicio, fim);
        usuarioSecurityAdapter.permissoesNivelUsuario(matricula);

        log.info("Consultando feriados no SARH...");
        var feriados = feriadoExternalClient.buscaFeriados(inicio, fim, null).
                stream().map(FeriadoJSarhResponse::toModel).toList();
        log.info("Total de feriados recuperados: {}", feriados.size());

        log.info("Vinculando usuário com seus dados do SARH...");
        var servidor = servidorService.atualizaDadosDoSarh(matricula);

        log.info("Consultando licenças, férias e ausências especiais do servidor no SARH...");
        servidor = servidorService.vinculaAusenciasServidorNoPeriodo(servidor, inicio, fim);
        log.info("Ausencias: {}", servidor.getAusencias().size());


        log.info("Carregando pontos para o período especificado...");
        var pontos = pontoService.carregaPontos(matricula, inicio, fim);
        log.info("Total de pontos recuperados: {}", pontos.size());

        var relatorioModel = processaDadosServidorParaRelatorio(servidor, pontos, feriados);

        log.info("Carregando arquivos do relatório...");
        var arquivoLogoImagemEsquerdaSuperior = arquivoRepository.findByNome(LOGO_SUPERIOR_ESQUERDO).
                orElseThrow(() -> new IllegalArgumentException(ATTRIB_NAO_ENCONTRADO.formatted(LOGO_SUPERIOR_ESQUERDO)));
        var arquivoLogoImagemDireitaSuperior = arquivoRepository.findByNome(LOGO_SUPERIOR_DIREITO).
                orElseThrow(() -> new IllegalArgumentException(ATTRIB_NAO_ENCONTRADO.formatted(LOGO_SUPERIOR_DIREITO)));
        var arquivoRelatorioPonto = arquivoRepository.findByNome(TEMPLATE_RELATORIO_USUARIO).
                orElseThrow(() -> new IllegalArgumentException(ATTRIB_NAO_ENCONTRADO.formatted(TEMPLATE_RELATORIO_USUARIO)));

        log.info("Mapeando parâmetros para o relatório...");
        var parametrosRelatorio = mapeandoParametrosRelatorio(relatorioModel,
                arquivoLogoImagemEsquerdaSuperior,
                arquivoLogoImagemDireitaSuperior,
                inicio, fim,
                relatorioModel.getPontosDataSource());


        var streamRelatorioPonto = new ByteArrayInputStream(arquivoRelatorioPonto.getBytes());
        var printRelatorioPonto = fillReport(streamRelatorioPonto, parametrosRelatorio, new JREmptyDataSource());
        return exportReportToPdf(printRelatorioPonto);
    }

}
