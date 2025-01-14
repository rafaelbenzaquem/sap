package br.jus.trf1.sap.intervalo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Setter
public class Intervalo {

    public static final String MSG_VALORES_INVALIDOS = "Intervalo com valores inválidos.";

    private LocalTime inicio;
    private LocalTime fim;

    public boolean eValido() {
        return inicio != null && fim != null && !inicio.isAfter(fim);
    }

    public long getIntervaloEmHoras() {
        if (eValido())
            return  Duration.between(inicio, fim).toHours();
        throw new IntervaloInvalidoException(MSG_VALORES_INVALIDOS);
    }

    public long getIntervaloEmMinutos() {
        if (eValido())
            return Duration.between(inicio, fim).toMinutes();
        throw new IntervaloInvalidoException(MSG_VALORES_INVALIDOS);
    }


    public long getIntervaloEmSegundos() {
        if (eValido())
            return Duration.between(inicio, fim).toSeconds();
        throw new IntervaloInvalidoException(MSG_VALORES_INVALIDOS);
    }

    @Override
    public String toString() {
        return "Intervalo{" +
                "inicio=" + inicio +
                ", fim=" + fim +
                '}';
    }
}
