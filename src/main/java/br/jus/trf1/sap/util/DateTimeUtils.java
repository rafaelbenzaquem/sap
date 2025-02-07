package br.jus.trf1.sap.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class DateTimeUtils {


    private static final String padraoData = "ddMMyyyy";

    private static final String padraoTempo = "HHmmss";

    private DateTimeUtils() {
    }

    public static LocalDateTime criaLocalDateTime(String textoData, String textoTempo) {
        return criaLocalDateTime(textoData, textoTempo, padraoData, padraoTempo);
    }

    public static LocalDateTime criaLocalDateTime(String textoData, String textoTempo, String padraoData, String padraoTempo) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern(padraoData);
        LocalDate data = LocalDate.parse(textoData, formatoData);
        DateTimeFormatter formatoTempo = DateTimeFormatter.ofPattern(padraoTempo);
        LocalTime tempo = LocalTime.parse(textoTempo, formatoTempo);
        return LocalDateTime.of(data, tempo);
    }

    public static LocalTime criaLocalTime(String textoTempo) {
        return criaLocalTime(textoTempo, padraoTempo);
    }

    public static LocalTime criaLocalTime(String textoTempo, String padraoTempo) {
        DateTimeFormatter formatoTempo = DateTimeFormatter.ofPattern(padraoTempo);
        return LocalTime.parse(textoTempo, formatoTempo);
    }

    public static LocalDate criaLocalDate(String textoData) {
        return criaLocalDate(textoData, padraoData);
    }

    public static String dataParaString(LocalDate data) {
        return dataParaString(data, padraoData);
    }
    public static String tempoParaString(LocalTime tempo) {
        return tempoParaString(tempo, padraoTempo);
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
