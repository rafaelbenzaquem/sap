package br.jus.trf1.sap.registro.web;

import br.jus.trf1.sap.registro.Registro;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;

public record RegistroResponse(@JsonFormat(pattern = "HH:mm:ss",shape = JsonFormat.Shape.STRING)
                               LocalTime hora,
                               String sentido,
                               @JsonProperty("codigo_acesso")
                               Integer codigoAcesso,
                               @JsonProperty("versao")
                               Integer versao
) {
    public static RegistroResponse of(Registro registro) {
        return new RegistroResponse(
                registro.getHora(),
                registro.getSentido().getPalavra(),
                registro.getCodigoAcesso(),
                registro.getVersao()
        );
    }
}
