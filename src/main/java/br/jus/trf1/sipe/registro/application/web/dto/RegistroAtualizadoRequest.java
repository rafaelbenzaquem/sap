package br.jus.trf1.sipe.registro.application.web.dto;

import br.jus.trf1.sipe.registro.domain.model.Registro;
import br.jus.trf1.sipe.registro.domain.model.Sentido;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_TEMPO;

public record RegistroAtualizadoRequest(@NotNull(message = "O campo 'id' não pode ser nulo!")
                                        Long id,
                                        @NotNull(message = "O campo 'hora' não pode ser nulo!")
                                        @JsonFormat(pattern = PADRAO_ENTRADA_TEMPO, shape = JsonFormat.Shape.STRING)
                                        LocalTime hora,
                                        @NotBlank(message = "O campo 'sentido' não pode ser branco ou nulo!")
                                        String sentido,
                                        @NotNull(message = "O campo 'ativo' não pode ser nulo!")
                                        Boolean ativo,
                                        @JsonProperty(value = "codigo_acesso")
                                        Integer codigoAcesso) {
    public Registro toModel() {
        return Registro.builder()
                .id(id)
                .hora(hora)
                .sentido(Sentido.toEnum(sentido))
                .codigoAcesso(codigoAcesso)
                .ativo(ativo)
                .build();
    }
}
