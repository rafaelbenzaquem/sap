package br.jus.trf1.sipe.relatorio;

import br.jus.trf1.sipe.arquivo.db.ArquivoRepository;
import br.jus.trf1.sipe.externo.jsarh.feriado.FeriadoExternalClient;
import br.jus.trf1.sipe.externo.jsarh.feriado.dto.FeriadoExternalResponse;
import br.jus.trf1.sipe.ponto.PontoRepository;
import br.jus.trf1.sipe.servidor.ServidorService;
import br.jus.trf1.sipe.usuario.UsuarioAtualService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

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
    public RelatorioUsuarioService(FeriadoExternalClient feriadoExternalClient, PontoRepository pontoRepository,
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
    public byte[] gerarRelatorio(String matricula, LocalDate inicio, LocalDate fim) throws JRException {

        log.info("Iniciando geração de relatório para matrícula: {}, período: {} a {}", matricula, inicio, fim);
        usuarioAtualService.permissoesNivelUsuario(matricula);

        log.info("Consultando feriados no SARH...");
        var feriados = feriadoExternalClient.buscaFeriados(inicio, fim, null).
                stream().map(FeriadoExternalResponse::toModel).toList();
        log.info("Total de feriados recuperados: {}", feriados.size());

        log.info("Vinculando usuário com seus dados do SARH...");
        var servidor = servidorService.vinculaUsuarioServidor(matricula);

        log.info("Consultando licenças, férias e ausências especiais do servidor no SARH...");
        servidor = servidorService.vinculaAusenciasServidorNoPeriodo(servidor, inicio, fim);
        log.info("Ausencias: {}", servidor.getAusencias().size());


        log.info("Carregando pontos para o período especificado...");
        var pontos = pontoRepository.buscaPontosPorPeriodo(matricula, inicio, fim);
        log.info("Total de pontos recuperados: {}", pontos.size());

        var relatorioModel = processaDadosServidorParaRelatorio(servidor, pontos, feriados);

        log.info("Carregando arquivos do relatório...");
        var arquivoLogoImagemEsquerdaSuperior = arquivoRepository.findByNome("logoImagem.png").
                orElseThrow(() -> new IllegalArgumentException("Arquivo 'logoImagem.png' não encontrado"));
        var arquivoLogoImagemDireitaSuperior = arquivoRepository.findByNome("logoImagem2.png").
                orElseThrow(() -> new IllegalArgumentException("Arquivo 'logoImagem2.png' não encontrado"));
        var arquivoRelatorioPonto = arquivoRepository.findByNome("relatorioA4.jasper").
                orElseThrow(() -> new IllegalArgumentException("Arquivo 'relatorioA4.jasper' não encontrado"));

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
