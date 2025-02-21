package br.jus.trf1.sap.relatorio.model.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import static br.jus.trf1.sap.relatorio.model.util.ConstatesRelatorioUtil.*;
import static br.jus.trf1.sap.util.ConstantesDataTempoUtil.PADRAO_SAIDA_DATA;
import static br.jus.trf1.sap.util.DataTempoUtil.dataParaString;

/**
 * Utilitário para formatação de textos no relatório.
 */
public class FomatadorTextoUtil {

    private FomatadorTextoUtil() {
    }


    /**
     * Formata a duração de permanência diária no formato HH:mm:ss.
     *
     * @param permanencia Duração da permanência.
     * @return Texto formatado.
     */
    public static String formataTextoTempoDiario(Duration permanencia) {
        var horas = permanencia.toHours();
        var minutos = permanencia.toMinutes();
        var segundos = permanencia.toSeconds();

        return PADRAO_TEXTO_TEMPO_DIARIO.formatted(horas,
                horas == 0 ? minutos : minutos % (horas * 60),
                minutos == 0 ? segundos : segundos % (minutos * 60));
    }

    /**
     * Formata a data e o dia da semana no formato "dd/MM/yyyy - DIA".
     *
     * @param dia Data a ser formatada.
     * @return Texto formatado.
     */
    public static String formataTextoDia(LocalDate dia) {
        return dataParaString(dia, PADRAO_SAIDA_DATA) + " - " + dia.getDayOfWeek().
                getDisplayName(TextStyle.SHORT, Locale.of("pt", "BR"));
    }

    /**
     * Formata o total de horas úteis no formato "%d horas, %d minutos, %d segundos".
     *
     * @param diasUteis   Número de dias úteis.
     * @param horasDiaria Horas diárias trabalhadas.
     * @return Texto formatado.
     */
    public static String formataTextoHorasUteis(long diasUteis, int horasDiaria) {
        return PADRAO_TEXTO_TEMPO_TOTAL.formatted(diasUteis * horasDiaria, 0, 0);
    }

    /**
     * Formata o rótulo de crédito ou débito com base na duração.
     *
     * @param horasDebitoOuCredito Duração do crédito ou débito.
     * @return Rótulo formatado.
     */
    public static String formataRotuloHorasCreditoOuDebito(Duration horasDebitoOuCredito) {
        return horasDebitoOuCredito.isPositive() || horasDebitoOuCredito.isZero() ?
                "Crédito - total de horas...:" :
                "Débito - total de horas....:";
    }

    /**
     * Formata a duração no formato "%d horas, %d minutos, %d segundos".
     *
     * @param duracao Duração a ser formatada.
     * @return Texto formatado.
     */
    public static String formataTextoDuracao(Duration duracao) {
        // Converte a duração total para horas, minutos e segundos
        long horasCD = duracao.toHours();
        long minutosCD = duracao.toMinutes();
        long segundosCD = duracao.toSeconds();

        // Calcula os minutos e segundos restantes
        long minutosRestantes = horasCD == 0
                ? minutosCD
                : minutosCD % (horasCD * 60);

        long segundosRestantes = minutosCD == 0
                ? segundosCD
                : segundosCD % (minutosCD * 60);

        // Formata o texto final usando o padrão definido
        return PADRAO_TEXTO_TEMPO_TOTAL.formatted(horasCD, minutosRestantes, segundosRestantes);
    }

    /**
     * Formata texto do período no formato "dd/MM/yyyy a dd/MM/yyyy".
     *
     * @param inicio Início do período.
     * @param fim Fim do período.
     * @return Texto formatado.
     */
    public static String formataTextoPeriodo(LocalDate inicio, LocalDate fim){
        return dataParaString(inicio, PADRAO_SAIDA_DATA) + " a " + dataParaString(fim, PADRAO_SAIDA_DATA);
    }
}
