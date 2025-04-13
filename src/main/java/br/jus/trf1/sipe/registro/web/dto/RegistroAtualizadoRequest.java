package br.jus.trf1.sipe.registro.web.dto;

import br.jus.trf1.sipe.registro.Registro;
import br.jus.trf1.sipe.registro.Sentido;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

import static br.jus.trf1.sipe.comum.util.ConstantesParaDataTempo.PADRAO_ENTRADA_TEMPO;

public record RegistroAtualizadoRequest(@NotNull(message = "O campo 'id' não pode ser nulo!")
                                        Long id,
                                        @NotNull(message = "O campo 'hora' não pode ser nulo!")
                                        @JsonFormat(pattern = PADRAO_ENTRADA_TEMPO, shape = JsonFormat.Shape.STRING)
                                        LocalTime hora,
                                        @NotBlank(message = "O campo 'sentido' não pode ser branco ou nulo!")
                                        String sentido,
                                        @NotNull(message = "O campo 'versao' não pode ser nulo!")
                                        Integer versao) {
    public Registro toModel() {
        return Registro.builder()
                .hora(hora)
                .sentido(Sentido.toEnum(sentido).getCodigo())
                .versao(versao + 1)
                .registroAnterior(Registro.builder()
                        .id(id)
                        .build())
                .build();
    }
}
