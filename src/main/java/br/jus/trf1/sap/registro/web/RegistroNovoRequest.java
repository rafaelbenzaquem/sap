package br.jus.trf1.sap.registro.web;

import br.jus.trf1.sap.registro.Registro;
import br.jus.trf1.sap.registro.Sentido;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record RegistroNovoRequest(@JsonFormat(pattern = "HH:mm:ss") LocalTime hora,
                                  String sentido) {
    public Registro toModel() {
        return Registro.builder()
                .hora(hora)
                .sentido(Sentido.toEnum(sentido).getCodigo())
                .build();
    }
}
