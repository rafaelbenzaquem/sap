package br.jus.trf1.sap.relatorio.model.util;

import br.jus.trf1.sap.externo.jsarh.ausencias.Ausencia;
import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.registro.Registro;
import br.jus.trf1.sap.registro.Sentido;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utilitário para cálculos relacionados a períodos de trabalho.
 */
@Slf4j
public class CalculadoraPeriodosUtil {

    private CalculadoraPeriodosUtil() {
    }

    /**
     * Calcula o número de dias úteis em uma lista de pontos.
     *
     * @param pontos Lista de pontos.
     * @return Número de dias úteis.
     */
    public static Long calculaDiasUteis(List<Ponto> pontos, Set<LocalDate> feriados) {
        log.debug("Calculando dias úteis...");
        return pontos.stream()
                .filter(p -> !p.getId().getDia().getDayOfWeek().equals(DayOfWeek.SATURDAY)) // Remove sábados
                .filter(p -> !p.getId().getDia().getDayOfWeek().equals(DayOfWeek.SUNDAY))  // Remove domingos
                .filter(p -> !feriados.contains(p.getId().getDia()))
                .count();
    }

    /**
     * Calcula a permanência total em uma lista de pontos.
     *
     * @param pontos Lista de pontos.
     * @return Duração total da permanência.
     */
    public static Duration calculaPermanenciaTotal(List<Ponto> pontos) {
        log.debug("Calculando permanencia total...");
        return pontos.stream().map(CalculadoraPeriodosUtil::calculaHorasPermanencia)
                .reduce(Duration.ZERO, Duration::plus);
    }

    /**
     * Calcula as horas de crédito ou débito com base na permanência total e dias úteis.
     *
     * @param permanenciaTotal Duração total da permanência.
     * @param diasUteis        Número de dias úteis.
     * @param horasDiarias     Horas diárias trabalhadas.
     * @return Duração do crédito ou débito.
     */
    public static Duration calculaHorasDebitoOuCredito(Duration permanenciaTotal, Long diasUteis, Integer horasDiarias) {
        log.debug("Calculando horas de crédito ou débito...");
        if (permanenciaTotal.toSeconds() > diasUteis * horasDiarias * 60 * 60) {
            return permanenciaTotal.minus(Duration.ofHours(diasUteis * horasDiarias));
        }
        return Duration.ofHours(diasUteis * horasDiarias).minus(permanenciaTotal).negated();
    }

    /**
     * Calcula a permanência em um ponto específico.
     *
     * @param ponto Ponto a ser calculado.
     * @return Duração da permanência.
     */
    public static Duration calculaHorasPermanencia(Ponto ponto) {
        log.debug("Calculando horas permanencia...");
        Duration totalHoras = Duration.ZERO;
        LocalTime entradaPendente = null;

        for (Registro registro : ponto.getRegistros()) {
            if (registro.getSentido() == Sentido.ENTRADA) {
                entradaPendente = registro.getHora();
            } else if (registro.getSentido() == Sentido.SAIDA && entradaPendente != null) {
                totalHoras = totalHoras.plus(Duration.between(entradaPendente, registro.getHora()));
                entradaPendente = null;
            }
        }
        return totalHoras;
    }

    /**
     * Retorna um conjunto de data a partir de uma lista de ausências
     *
     * @param ausencias Lista de período de ausências
     * @return conjunto de LocalDate
     */
    public static Set<LocalDate> gerarDiasAusentes(List<Ausencia> ausencias) {
        Set<LocalDate> diasAusentes = new HashSet<>();

        for (Ausencia ausencia : ausencias) {
            LocalDate dataAtual = ausencia.getInicio();
            while (!dataAtual.isAfter(ausencia.getFim())) { // Enquanto a data atual não for depois do fim
                diasAusentes.add(dataAtual); // Adiciona a data atual ao conjunto
                dataAtual = dataAtual.plusDays(1); // Avança para o próximo dia
            }
        }

        return diasAusentes;
    }
}
