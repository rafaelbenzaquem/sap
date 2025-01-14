package br.jus.trf1.sap.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FormataLocalDateTime {

    private FormataLocalDateTime() {
    }

    public static LocalDateTime criaLocalDateTime(String textoData, String textoTempo) {
        return criaLocalDateTime(textoData, textoTempo, "ddMMyyyy", "HHmmss");
    }

    public static LocalDateTime criaLocalDateTime(String textoData, String textoTempo, String padraoData, String padraoTempo) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern(padraoData);
        LocalDate data = LocalDate.parse(textoData, formatoData);
        DateTimeFormatter formatoTempo = DateTimeFormatter.ofPattern(padraoTempo);
        LocalTime tempo = LocalTime.parse(textoTempo, formatoTempo);
        return LocalDateTime.of(data, tempo);
    }

    public static LocalTime criaLocalTime(String textoTempo) {
        return criaLocalTime(textoTempo, "HHmmss");
    }

    public static LocalTime criaLocalTime(String textoTempo, String padraoTempo) {
        DateTimeFormatter formatoTempo = DateTimeFormatter.ofPattern(padraoTempo);
        return LocalTime.parse(textoTempo, formatoTempo);
    }

    public static LocalDate criaLocalDate(String textoData) {
        return criaLocalDate(textoData, "ddMMyyyy");
    }


    public static LocalDate criaLocalDate(String textoData, String padraoData) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern(padraoData);
        return LocalDate.parse(textoData, formatoData);
    }
}
