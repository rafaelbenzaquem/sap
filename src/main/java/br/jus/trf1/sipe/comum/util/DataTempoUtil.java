package br.jus.trf1.sipe.comum.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_TEMPO;


public class DataTempoUtil {

    private DataTempoUtil() {
    }

    public static LocalDateTime paraLocalDateTime(String textoData, String textoTempo) {
        return paraLocalDateTime(textoData, textoTempo, PADRAO_ENTRADA_DATA, PADRAO_ENTRADA_TEMPO);
    }

    public static LocalDateTime paraLocalDateTime(String textoData, String textoTempo, String padraoData, String padraoTempo) {
        LocalDate data = paraLocalDate(textoData, padraoData);
        LocalTime tempo = paraLocalTime(textoTempo, padraoTempo);
        return LocalDateTime.of(data, tempo);
    }

    public static LocalTime paraLocalTime(String textoTempo) {
        return paraLocalTime(textoTempo, PADRAO_ENTRADA_TEMPO);
    }

    public static LocalTime paraLocalTime(String textoTempo, String padraoTempo) {
        DateTimeFormatter formatoTempo = DateTimeFormatter.ofPattern(padraoTempo);
        return LocalTime.parse(textoTempo, formatoTempo);
    }

    public static LocalDate paraLocalDate(String textoData) {
        return paraLocalDate(textoData, PADRAO_ENTRADA_DATA);
    }

    public static LocalDate paraLocalDate(String textoData, String padraoData) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern(padraoData);
        return LocalDate.parse(textoData, formatoData);
    }
    public static String paraString(LocalDate data) {
        return paraString(data, PADRAO_ENTRADA_DATA);
    }

    public static String paraString(LocalTime tempo) {
        return paraString(tempo, PADRAO_ENTRADA_TEMPO);
    }

    public static String paraString(LocalDate data, String padraoData) {
        return data.format(DateTimeFormatter.ofPattern(padraoData));
    }

    public static String paraString(LocalTime tempo, String padraoTempo) {
        return tempo.format(DateTimeFormatter.ofPattern(padraoTempo));
    }
}
