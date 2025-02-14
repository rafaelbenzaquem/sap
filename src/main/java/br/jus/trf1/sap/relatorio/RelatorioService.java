package br.jus.trf1.sap.relatorio;

import br.jus.trf1.sap.arquivo.ArquivoRepository;
import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoRepository;
import br.jus.trf1.sap.relatorio.model.PontoRelatorioTableModel;
import br.jus.trf1.sap.relatorio.model.RegistroRelatorioListModel;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.IntStream;

import static br.jus.trf1.sap.util.DateTimeUtils.dataParaString;
import static br.jus.trf1.sap.util.DateTimeUtils.tempoParaString;
import static net.sf.jasperreports.engine.JasperExportManager.exportReportToPdf;
import static net.sf.jasperreports.engine.JasperFillManager.fillReport;

@Service
public class RelatorioService {


    private static final String PADRAO_DATA = "dd/MM/yyyy";
    private static final String PADRAO_TEMPO = "HH:mm:ss";
    private static final Logger log = LoggerFactory.getLogger(RelatorioService.class);
    private final VinculoRepository vinculoRepository;
    private final PontoRepository pontoRepository;
    private final ArquivoRepository arquivoRepository;

    public RelatorioService(VinculoRepository vinculoRepository, PontoRepository pontoRepository, ArquivoRepository arquivoRepository) {
        this.vinculoRepository = vinculoRepository;
        this.pontoRepository = pontoRepository;
        this.arquivoRepository = arquivoRepository;
    }

    public byte[] gerarRelatorio(Integer matricula, LocalDate inicio, LocalDate fim) throws JRException {

        var logoImagem = arquivoRepository.findByNome("logoImagem.png");
        var logoImagem2 = arquivoRepository.findByNome("logoImagem2.png");
        var relatorioPonto = arquivoRepository.findByNome("relatorioA4.jasper");
        var pontos = pontoRepository.buscarPontosPorMatriculaMaisRangeDeData(matricula, inicio, fim);
        var vinculoByMatricula = vinculoRepository.findVinculoByMatricula(matricula);
        var vinculo = vinculoByMatricula.orElseThrow();
        Map<String, Object> parametrosPonto = new HashMap<>();

        List<PontoRelatorioTableModel> pontosModels = new ArrayList<>();


        var sizePontos = pontos.size();
        log.info("Relatorio de Pontos - total = {}", sizePontos);
        pontos.forEach(p -> {
            log.info("Ponto {}", p);
            List<RegistroRelatorioListModel> relatoriosDataSource = new ArrayList<>();
            p.getRegistros().forEach(
                    r ->
                            relatoriosDataSource.add(
                                    RegistroRelatorioListModel.builder()
                                            .sentido(String.valueOf(r.getSentido()))
                                            .hora(tempoParaString(r.getHora(), PADRAO_TEMPO))
                                            .build()
                            )
            );

            pontosModels.add(PontoRelatorioTableModel.builder()
                    .dia(dataParaString(p.getId().getDia(), PADRAO_DATA) + " - " + p.getId().getDia().getDayOfWeek().
                            getDisplayName(TextStyle.SHORT, Locale.of("pt", "BR")))
                    .descricao(" ")
                    .registros(relatoriosDataSource)
                    .build());
        });


        var pontosDataSource = new JRBeanCollectionDataSource(pontosModels);


        parametrosPonto.put("pontosDataSource", pontosDataSource);
        parametrosPonto.put("logoImagem", logoImagem.orElseThrow().getConteudo());
        parametrosPonto.put("logo2Imagem", logoImagem2.orElseThrow().getConteudo());
        parametrosPonto.put("nome", vinculo.getNome());
        parametrosPonto.put("cargo", "TÉCNICO JUDICIÁRIO/ APOIO ESPECIALIZADO (TECNOLOGIA DA INFORMAÇÃO)");
        parametrosPonto.put("funcao", "ASSISTENTE ADJUNTO III");
        parametrosPonto.put("matricula", "RR" + matricula);
        parametrosPonto.put("lotacao", "SERVIÇO DE SUPORTE TÉCNICO AOS USUÁRIOS/SERSUT/SEINF/NUCAD/SECAD/SJRR");
        parametrosPonto.put("periodo", dataParaString(inicio, PADRAO_DATA) + " a " + dataParaString(fim, PADRAO_DATA));


        var streamRelatorioPonto = new ByteArrayInputStream(relatorioPonto.orElseThrow().getConteudo());
        var printRelatorioPonto = fillReport(streamRelatorioPonto, parametrosPonto, new JREmptyDataSource());
        return exportReportToPdf(printRelatorioPonto);

    }

}
