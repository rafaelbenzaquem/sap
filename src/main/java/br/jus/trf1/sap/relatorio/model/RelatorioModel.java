package br.jus.trf1.sap.relatorio.model;

import br.jus.trf1.sap.ponto.Ponto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public class RelatorioModel {


    private static final String PADRAO_TEMPO_TOTAL = "%d horas ,%d minutos ,%d segundos";

    private final UsuarioModel usuario;
    private final List<Ponto> pontos;
    private final Long diasUteis;
    private final String textoHorasUteis;

    private final Duration permanenciaTotal;
    private final String textoPermanenciaTotal;
    private final String rotuloHorasDebitoOuCredito;
    private final String textoHorasDebitoOuCredito;

    private final List<PontoModel> pontoModels = new ArrayList<>();
    private final JRBeanCollectionDataSource pontosDataSource;


    public RelatorioModel(UsuarioModel usuario, List<Ponto> pontos) {
        this.usuario = usuario;
        this.pontos = pontos;
        this.diasUteis = pontos.stream()
                .filter(p -> !p.getId().getDia().getDayOfWeek().equals(DayOfWeek.SATURDAY)) // Remove sábados
                .filter(p -> !p.getId().getDia().getDayOfWeek().equals(DayOfWeek.SUNDAY))  // Remove domingos
                .count();
        this.textoHorasUteis = constroiTextoHorasUteis();
        this.permanenciaTotal = calculaPermanenciaTotal();
        this.textoPermanenciaTotal = constroiTextoPernanenciaTotal(permanenciaTotal);
        this.rotuloHorasDebitoOuCredito = constroiRotuloHorasDebitoOuCredito();
        this.textoHorasDebitoOuCredito = constroiTextoHorasDebitoOuCredito();
        carregarDadosPontos();
        this.pontosDataSource = new JRBeanCollectionDataSource(pontoModels, false);
    }

    private String constroiTextoHorasDebitoOuCredito() {
        // Converte a duração total para horas, minutos e segundos
        long horasTotal = permanenciaTotal.toHours();
        long minutosTotal = permanenciaTotal.toMinutes();
        long segundosTotal = permanenciaTotal.toSeconds();

        // Calcula o débito/crédito em horas, minutos e segundos
        long horasDebitoCredito = horasTotal - diasUteis * 7;
        long minutosDebitoCredito = minutosTotal - diasUteis * 7 * 60;
        long segundosDebitoCredito = segundosTotal - diasUteis * 7 * 60 * 60;

        // Garante que os valores sejam absolutos (positivos)
        horasDebitoCredito = Math.abs(horasDebitoCredito);
        minutosDebitoCredito = Math.abs(minutosDebitoCredito);
        segundosDebitoCredito = Math.abs(segundosDebitoCredito);

        // Calcula os minutos e segundos restantes após subtrair as horas e minutos completos
        long minutosRestantes = horasDebitoCredito == 0
                ? minutosDebitoCredito
                : minutosDebitoCredito % (horasDebitoCredito * 60);

        long segundosRestantes = minutosDebitoCredito == 0
                ? segundosDebitoCredito
                : segundosDebitoCredito % (minutosDebitoCredito * 60);

        // Formata o texto final usando o padrão definido
        return PADRAO_TEMPO_TOTAL.formatted(horasDebitoCredito, minutosRestantes, segundosRestantes);
    }

    private String constroiRotuloHorasDebitoOuCredito() {
        var segundos = permanenciaTotal.toSeconds();
        var segundosDC = segundos - diasUteis * 7 * 60 * 60;
        var isCredito = segundosDC >= 0;
        return isCredito ? "Crédito - total de horas...:" : "Débito - total de horas....:";
    }

    private Duration calculaPermanenciaTotal() {
        return pontos.stream().map(Ponto::calculaHorasRegistros)
                .reduce(Duration.ZERO, Duration::plus);
    }

    private String constroiTextoPernanenciaTotal(Duration permanenciaTotal) {
        var horas = permanenciaTotal.toHours();
        var minutos = permanenciaTotal.toMinutes();
        var segundos = permanenciaTotal.toSeconds();

        return PADRAO_TEMPO_TOTAL.formatted(
                horas,
                horas == 0 ? minutos : minutos % (horas * 60),
                minutos == 0 ? segundos : segundos % (minutos * 60)
        );
    }

    private void carregarDadosPontos() {
        var sizePontos = pontos.size();
        log.info("Relatorio de Pontos - total = {}", sizePontos);
        pontos.forEach(ponto -> {
            log.info("Ponto {}", ponto);
            var pontoRelatorioTableModel = new PontoModel(ponto);

            pontoModels.add(pontoRelatorioTableModel);
        });
    }

    private String constroiTextoHorasUteis() {
        return PADRAO_TEMPO_TOTAL.formatted(diasUteis * 7, 0, 0);
    }
}
