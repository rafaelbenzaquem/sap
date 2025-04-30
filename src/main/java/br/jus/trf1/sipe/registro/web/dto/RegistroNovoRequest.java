package br.jus.trf1.sipe.registro.web.dto;

import br.jus.trf1.sipe.registro.Registro;
import br.jus.trf1.sipe.registro.Sentido;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_TEMPO;

public record RegistroNovoRequest(@NotNull(message = "O campo 'hora' não pode ser nulo!")
                                  @JsonFormat(pattern = PADRAO_ENTRADA_TEMPO, shape = JsonFormat.Shape.STRING)
                                  LocalTime hora,
                                  @NotBlank(message = "O campo 'sentido' não pode ser branco ou nulo!")
                                  String sentido,
                                  @JsonProperty(value = "codigo_acesso")
                                  Integer codigoAcesso) {
    public Registro toModel() {
        return Registro.builder()
                .hora(hora)
                .sentido(Sentido.toEnum(sentido).getCodigo())
                .codigoAcesso(codigoAcesso)
                .build();
    }
}
