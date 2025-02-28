package br.jus.trf1.sap.relatorio;

import br.jus.trf1.sap.arquivo.ArquivoRepository;
import br.jus.trf1.sap.externo.jsarh.ausencias.Ausencia;
import br.jus.trf1.sap.externo.jsarh.ausencias.especial.EspecialService;
import br.jus.trf1.sap.externo.jsarh.ausencias.especial.dto.EspecialResponse;
import br.jus.trf1.sap.externo.jsarh.ausencias.ferias.FeriasService;
import br.jus.trf1.sap.externo.jsarh.ausencias.ferias.dto.FeriasResponse;
import br.jus.trf1.sap.externo.jsarh.ausencias.licenca.LicencasService;
import br.jus.trf1.sap.externo.jsarh.ausencias.licenca.dto.LicencaResponse;
import br.jus.trf1.sap.externo.jsarh.feriado.FeriadoService;
import br.jus.trf1.sap.externo.jsarh.feriado.dto.FeriadoResponse;
import br.jus.trf1.sap.externo.jsarh.servidor.ServidorService;
import br.jus.trf1.sap.ponto.PontoRepository;
import br.jus.trf1.sap.relatorio.model.RelatorioModel;
import br.jus.trf1.sap.relatorio.model.UsuarioModel;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static br.jus.trf1.sap.relatorio.model.util.FomatadorTextoUtil.formataTextoPeriodo;
import static net.sf.jasperreports.engine.JasperExportManager.exportReportToPdf;
import static net.sf.jasperreports.engine.JasperFillManager.fillReport;

/**
 * Serviço responsável por gerar relatórios de pontos.
 * Utiliza JasperReports para criar relatórios em PDF com base nos dados fornecidos.
 */
@Slf4j
@Service
public class RelatorioService {


    private final FeriadoService feriadoService;
    private final PontoRepository pontoRepository;
    private final ArquivoRepository arquivoRepository;
    private final ServidorService servidorService;
    private final FeriasService feriasService;
    private final EspecialService especialService;
    private final LicencasService licencasService;

    /**
     * Constrói o serviço de relatório com as dependências necessárias.
     *
     * @param feriadoService    Repositório de vínculos.
     * @param pontoRepository   Repositório de pontos.
     * @param arquivoRepository Repositório de arquivos.
     * @param servidorService   Serviço de acesso a dados do Servidor no Sarh
     */
    public RelatorioService(FeriadoService feriadoService, PontoRepository pontoRepository,
                            ArquivoRepository arquivoRepository, ServidorService servidorService,
                            FeriasService feriasService, EspecialService especialService,
                            LicencasService licencasService) {
        this.feriadoService = feriadoService;
        this.pontoRepository = pontoRepository;
        this.arquivoRepository = arquivoRepository;
        this.servidorService = servidorService;
        this.feriasService = feriasService;
        this.especialService = especialService;
        this.licencasService = licencasService;
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
    public byte[] gerarRelatorio(Integer matricula, LocalDate inicio, LocalDate fim) throws JRException {

        log.debug("Iniciando geração de relatório para matrícula: {}, período: {} a {}", matricula, inicio, fim);

        log.debug("Carregando imagens e arquivo de relatório...");
        var logoImagem = arquivoRepository.findByNome("logoImagem.png").
                orElseThrow(() -> new IllegalArgumentException("Arquivo 'logoImagem.png' não encontrado"));
        var logoImagem2 = arquivoRepository.findByNome("logoImagem2.png").
                orElseThrow(() -> new IllegalArgumentException("Arquivo 'logoImagem2.png' não encontrado"));
        var arquivoRelatorioPonto = arquivoRepository.findByNome("relatorioA4.jasper").
                orElseThrow(() -> new IllegalArgumentException("Arquivo 'relatorioA4.jasper' não encontrado"));

        log.debug("Carregando pontos para o período especificado...");
        var pontos = pontoRepository.buscaPontosPorMatriculaEmPeriodoDetermiando(matricula, inicio, fim);
        log.debug("Total de pontos recuperados: {}", pontos.size());


        var servidor = servidorService.buscaDadosServidor("RR" + matricula);

        log.debug("Consultando feriados no SARH...");
        var feriados = feriadoService.buscaFeriados(inicio, fim, null).
                stream().map(FeriadoResponse::toModel).toList();
        var licencas = licencasService.buscaLicenca("RR" + matricula, inicio, fim).
                stream().map(LicencaResponse::toModel).toList();

        var especiais = especialService.buscaAusenciasEspeciais("RR" + matricula, inicio, fim).
                stream().map(EspecialResponse::toModel).toList();

        var ferias = feriasService.buscaFerias("RR" + matricula, inicio, inicio).
                stream().map(FeriasResponse::toModel).toList();

        var ausencias = new ArrayList<Ausencia>(licencas);
        ausencias.addAll(especiais);
        ausencias.addAll(ferias);

        log.info("Ausencias: {}", ausencias.size());

        log.info("Feriados: {}", feriados.size());

        log.debug("Construindo modelo de usuário...");
        var usuario = UsuarioModel.builder()
                .nome(servidor.getNome())
                .cargo(servidor.getCargo() == null ? "Servidor Requisitado" : servidor.getCargo())
                .funcao(servidor.getFuncao())
                .lotacao(servidor.getDescricaoLotacao())
                .matricula(matricula)
                .horasDiaria(7)
                .build();
        log.debug("Construindo modelo de relatório...");


        var relatorioModel = new RelatorioModel(usuario, pontos);
        var relatorioModel = new RelatorioModel(usuario, pontos, feriados, ausencias);

        log.debug("Preparando parâmetros para o relatório...");
        var parametrosRelatorio = new HashMap<String, Object>();

        parametrosRelatorio.put("logoImagem", logoImagem.getConteudo());
        parametrosRelatorio.put("logoImagem2", logoImagem2.getConteudo());
        parametrosRelatorio.put("obs", "Sem observações...");
        parametrosRelatorio.put("totalHorasUteis", relatorioModel.getTextoHorasUteis());
        parametrosRelatorio.put("totalHorasPermanencia", relatorioModel.getTextoPermanenciaTotal());
        parametrosRelatorio.put("creditoDebitoLabel", relatorioModel.getRotuloHorasCreditoOuDebito());
        parametrosRelatorio.put("horaCDTotal", relatorioModel.getTextoHorasCreditoOuDebito());

        parametrosRelatorio.put("pontosDataSource", relatorioModel.getPontosDataSource());

        parametrosRelatorio.put("nome", usuario.nome());
        parametrosRelatorio.put("cargo", usuario.cargo());
        parametrosRelatorio.put("funcao", usuario.funcao());
        parametrosRelatorio.put("matricula", "RR" + usuario.matricula());
        parametrosRelatorio.put("lotacao", usuario.lotacao());
        parametrosRelatorio.put("periodo", formataTextoPeriodo(inicio, fim));

        var streamRelatorioPonto = new ByteArrayInputStream(arquivoRelatorioPonto.getConteudo());
        var printRelatorioPonto = fillReport(streamRelatorioPonto, parametrosRelatorio, new JREmptyDataSource());
        return exportReportToPdf(printRelatorioPonto);

    }
}
