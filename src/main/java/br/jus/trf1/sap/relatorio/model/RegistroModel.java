package br.jus.trf1.sap.relatorio.model;

import lombok.Getter;

import java.time.LocalTime;

import static br.jus.trf1.sap.util.DateTimeUtils.tempoParaString;

@Getter
public class RegistroModel {

    private static final String PADRAO_TEMPO = "HH:mm:ss";
    public static final RegistroModel VAZIO = new RegistroModel("-----",LocalTime.MIN,"--:--");

    private final String sentido;
    private final LocalTime hora;
    private final String textoHora;

    private RegistroModel(String sentido, LocalTime hora, String textoHora) {
        this.sentido = sentido;
        this.hora = hora;
        this.textoHora = textoHora;
    }

    public static RegistroModel of(String sentido, LocalTime hora) {
        var textoHora = tempoParaString(hora, PADRAO_TEMPO);
        return new RegistroModel(sentido, hora, textoHora);
    }


}
