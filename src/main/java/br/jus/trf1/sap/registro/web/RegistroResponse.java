package br.jus.trf1.sap.registro.web;

import br.jus.trf1.sap.registro.Registro;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;

public record RegistroResponse(@JsonFormat(pattern = "HH:mm:ss")
                               LocalTime hora,
                               String sentido,
                               @JsonProperty("codigo_acesso")
                               Integer codigoAcesso,
                               @JsonProperty("id_registro_nova_versao")
                               Long registroProximo
) {
    public static RegistroResponse of(Registro registro) {
        return new RegistroResponse(
                registro.getHora(),
                registro.getSentido().getPalavra(),
                registro.getCodigoAcesso(),
                registro.getRegistroAtualizado() == null ? null : registro.getRegistroAtualizado().getId()
        );
    }
}
