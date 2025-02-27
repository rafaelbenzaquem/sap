package br.jus.trf1.sap.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static br.jus.trf1.sap.util.ConstantesDataTempoUtil.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sap.util.ConstantesDataTempoUtil.PADRAO_ENTRADA_TEMPO;


public class DataTempoUtil {

    private DataTempoUtil() {
    }

    public static LocalDateTime criaLocalDateTime(String textoData, String textoTempo) {
        return criaLocalDateTime(textoData, textoTempo, PADRAO_ENTRADA_DATA, PADRAO_ENTRADA_TEMPO);
    }

    public static LocalDateTime criaLocalDateTime(String textoData, String textoTempo, String padraoData, String padraoTempo) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern(padraoData);
        LocalDate data = LocalDate.parse(textoData, formatoData);
        DateTimeFormatter formatoTempo = DateTimeFormatter.ofPattern(padraoTempo);
        LocalTime tempo = LocalTime.parse(textoTempo, formatoTempo);
        return LocalDateTime.of(data, tempo);
    }

    public static LocalTime criaLocalTime(String textoTempo) {
        return criaLocalTime(textoTempo, PADRAO_ENTRADA_TEMPO);
    }

    public static LocalTime criaLocalTime(String textoTempo, String padraoTempo) {
        DateTimeFormatter formatoTempo = DateTimeFormatter.ofPattern(padraoTempo);
        return LocalTime.parse(textoTempo, formatoTempo);
    }

    public static LocalDate criaLocalDate(String textoData) {
        return criaLocalDate(textoData, PADRAO_ENTRADA_DATA);
    }

    public static String dataParaString(LocalDate data) {
        return dataParaString(data, PADRAO_ENTRADA_DATA);
    }

    public static String tempoParaString(LocalTime tempo) {
        return tempoParaString(tempo, PADRAO_ENTRADA_TEMPO);
    }

    public static String dataParaString(LocalDate data, String padraoData) {
        return data.format(DateTimeFormatter.ofPattern(padraoData));
    }

    public static String tempoParaString(LocalTime tempo, String padraoTempo) {
        return tempo.format(DateTimeFormatter.ofPattern(padraoTempo));
    }

    public static LocalDate criaLocalDate(String textoData, String padraoData) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern(padraoData);
        return LocalDate.parse(textoData, formatoData);
    }
}
