package br.jus.trf1.sap.relatorio;

import br.jus.trf1.sap.arquivo.ArquivoRepository;
import br.jus.trf1.sap.ponto.PontoRepository;
import br.jus.trf1.sap.relatorio.model.PontoRelatorioTableModel;
import br.jus.trf1.sap.relatorio.model.RegistroRelatorioListModel;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.IntStream;

import static br.jus.trf1.sap.util.DateTimeUtils.dataParaString;
import static br.jus.trf1.sap.util.DateTimeUtils.tempoParaString;
import static net.sf.jasperreports.engine.JasperExportManager.exportReportToPdf;
import static net.sf.jasperreports.engine.JasperFillManager.fillReport;

@Slf4j
@Service
public class RelatorioService {


    private static final String PADRAO_DATA = "dd/MM/yyyy";
    private static final String PADRAO_TEMPO = "HH:mm:ss";
    private final VinculoRepository vinculoRepository;
    private final PontoRepository pontoRepository;
    private final ArquivoRepository arquivoRepository;

    public RelatorioService(VinculoRepository vinculoRepository, PontoRepository pontoRepository, ArquivoRepository arquivoRepository) {
        this.vinculoRepository = vinculoRepository;
        this.pontoRepository = pontoRepository;
        this.arquivoRepository = arquivoRepository;
    }

    public byte[] gerarRelatorio(Integer matricula, LocalDate inicio, LocalDate fim) throws JRException {

        log.info("Gerando Relatorio");
        log.info("Carregando image da logo superior esquerda: {}", "logoImagem.png");
        var logoImagem = arquivoRepository.findByNome("logoImagem.png");
        log.info("Carregando image da logo superior direita: {}", "logoImagem2.png");
        var logoImagem2 = arquivoRepository.findByNome("logoImagem2.png");
        log.info("Carregando arquivo de relatório Jasper: {}", "relatorioA4.jasper");
        var relatorioPonto = arquivoRepository.findByNome("relatorioA4.jasper");

        var pontos = pontoRepository.buscarPontosPorMatriculaMaisRangeDeData(matricula, inicio, fim);
        var vinculoByMatricula = vinculoRepository.findVinculoByMatricula(matricula);
        var vinculo = vinculoByMatricula.orElseThrow();

        Map<String, Object> parametrosPonto = new HashMap<>();
        List<PontoRelatorioTableModel> pontosModels = new ArrayList<>();


        List<Duration> permanencias = new ArrayList<>();

        var sizePontos = pontos.size();
        log.info("Relatorio de Pontos - total = {}", sizePontos);
        pontos.forEach(p -> {
            log.info("Ponto {}", p);
            List<RegistroRelatorioListModel> relatoriosDataSource = new ArrayList<>();
            var registros = p.getRegistros();
            var permanencia = p.calculaHorasRegistros();
            IntStream.range(0, 12).forEach(
                    index -> {
                        if (index < registros.size()) {
                            var r = registros.get(index);
                            relatoriosDataSource.add(
                                    RegistroRelatorioListModel.builder()
                                            .sentido(r.getSentido().getPalavra())
                                            .hora(tempoParaString(r.getHora(), PADRAO_TEMPO))
                                            .build()
                            );
                        } else {
                            relatoriosDataSource.add(
                                    RegistroRelatorioListModel.builder()
                                            .sentido("-----")
                                            .hora("--:--")
                                            .build());
                        }
                    }
            );

            var horas = permanencia.toHours();
            var minutos = permanencia.toMinutes();
            var segundos = permanencia.toSeconds();

            relatoriosDataSource.add(
                    RegistroRelatorioListModel.builder()
                            .sentido("Total")
                            .hora("%02d:%02d:%02d".formatted(horas,
                                    horas == 0 ? minutos : minutos % (horas * 60),
                                    minutos == 0 ? segundos : segundos % (minutos * 60)))
                            .build());

            permanencias.add(permanencia);

            pontosModels.add(PontoRelatorioTableModel.builder()
                    .dia(dataParaString(p.getId().getDia(), PADRAO_DATA) + " - " + p.getId().getDia().getDayOfWeek().
                            getDisplayName(TextStyle.SHORT, Locale.of("pt", "BR")))
                    .descricao(" ")
                    .registros(relatoriosDataSource)
                    .build());
        });


        long diasUteis = pontos.stream()
                .filter(data -> !data.getId().getDia().getDayOfWeek().equals(DayOfWeek.SATURDAY)) // Remove sábados
                .filter(data -> !data.getId().getDia().getDayOfWeek().equals(DayOfWeek.SUNDAY))  // Remove domingos
                // Remove feriados
                .count();
        log.info("Dias úteis encontrados - total = {}", diasUteis);

        var permanenciaTotal = permanencias.stream().reduce(Duration.ZERO, Duration::plus);
        String patternTotalHora = "%d horas ,%d minutos ,%d segundos";

        var horas = permanenciaTotal.toHours();
        var minutos = permanenciaTotal.toMinutes();
        var segundos = permanenciaTotal.toSeconds();

        var totalHorasUteis = patternTotalHora.formatted(diasUteis * 7, 0, 0);

        parametrosPonto.put("totalHorasUteis", totalHorasUteis);
        log.info("Horas - total de horas = {}", totalHorasUteis);


        var totalHorasPermanencia = patternTotalHora.formatted(horas,
                horas == 0 ? minutos : minutos % (horas * 60),
                minutos == 0 ? segundos : segundos % (minutos * 60));

        parametrosPonto.put("totalHorasPermanencia", totalHorasPermanencia);
        log.info("Permanência - total de horas = {}", totalHorasPermanencia);

        var horasDC = horas - diasUteis * 7;
        log.info("Total de horas de {} = {}", tipoOperacao(horasDC), horasDC);
        var minutosDC = minutos - diasUteis * 7 * 60;
        log.info("Total de minutos de {} = {}", tipoOperacao(minutosDC), minutosDC);
        var segundosDC = segundos - diasUteis * 7 * 60 * 60;
        var tipoOperacao = tipoOperacao(segundosDC);
        var isCredito = segundosDC >= 0;
        log.info("Total de segundos de {} = {}", tipoOperacao, segundosDC);
        horasDC = Math.abs(horasDC);
        minutosDC = Math.abs(minutosDC);
        segundosDC = Math.abs(segundosDC);

        var creditoDebitoLabel = isCredito ? "Crédito - total de horas...:" : "Débito - total de horas....:";
        var horaCDTotal = patternTotalHora.formatted(horasDC,
                horasDC == 0 ? minutosDC : minutosDC > (horasDC * 60) ? minutosDC % (horasDC * 60) : (horasDC * 60) % minutosDC,
                minutosDC == 0 ? segundosDC : segundosDC > (minutosDC * 60) ? segundosDC % (minutosDC * 60) : (minutosDC * 60) % segundosDC);

        parametrosPonto.put("creditoDebitoLabel", creditoDebitoLabel);
        parametrosPonto.put("horaCDTotal", horaCDTotal);
        log.info("{} - total de horas:{}", tipoOperacao, horaCDTotal);

        var pontosDataSource = new JRBeanCollectionDataSource(pontosModels);

        parametrosPonto.put("obs", "Sem observações...");
        parametrosPonto.put("pontosDataSource", pontosDataSource);
        parametrosPonto.put("logoImagem", logoImagem.orElseThrow().getConteudo());
        parametrosPonto.put("logoImagem2", logoImagem2.orElseThrow().getConteudo());
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


    private String tipoOperacao(long tempo) {
        return tempo < 0 ? "Débito" : "Crédito";
    }
}
