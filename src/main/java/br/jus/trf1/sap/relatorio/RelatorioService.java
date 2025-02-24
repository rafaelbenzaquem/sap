package br.jus.trf1.sap.relatorio;

import br.jus.trf1.sap.arquivo.ArquivoRepository;
import br.jus.trf1.sap.ponto.PontoRepository;
import br.jus.trf1.sap.relatorio.model.RelatorioModel;
import br.jus.trf1.sap.relatorio.model.UsuarioModel;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
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


    private final VinculoRepository vinculoRepository;
    private final PontoRepository pontoRepository;
    private final ArquivoRepository arquivoRepository;

    /**
     * Constrói o serviço de relatório com as dependências necessárias.
     *
     * @param vinculoRepository  Repositório de vínculos.
     * @param pontoRepository    Repositório de pontos.
     * @param arquivoRepository  Repositório de arquivos.
     */
    public RelatorioService(VinculoRepository vinculoRepository, PontoRepository pontoRepository, ArquivoRepository arquivoRepository) {
        this.vinculoRepository = vinculoRepository;
        this.pontoRepository = pontoRepository;
        this.arquivoRepository = arquivoRepository;
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


        log.debug("Carregando vinculo por matrícula {}...", matricula);
        var vinculoByMatricula = vinculoRepository.findVinculoByMatricula(matricula).
        orElseThrow(() -> new IllegalArgumentException("Vínculo não encontrado para a matrícula: " + matricula));

        log.debug("Construindo modelo de usuário...");
        var usuario = UsuarioModel.builder()
                .nome(vinculoByMatricula.getNome())
                .cargo("TÉCNICO JUDICIÁRIO/ APOIO ESPECIALIZADO (TECNOLOGIA DA INFORMAÇÃO)")
                .funcao("ASSISTENTE ADJUNTO III")
                .lotacao("SERVIÇO DE SUPORTE TÉCNICO AOS USUÁRIOS/SERSUT/SEINF/NUCAD/SECAD/SJRR")
                .matricula(matricula)
                .horasDiaria(7)
                .build();
        log.debug("Construindo modelo de relatório...");


        var relatorioModel = new RelatorioModel(usuario, pontos);

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
