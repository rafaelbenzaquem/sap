package br.jus.trf1.sipe.registro.application.web.dto;

import br.jus.trf1.sipe.registro.domain.model.Registro;
import br.jus.trf1.sipe.registro.domain.model.Sentido;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalTime;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_TEMPO;

@Relation(collectionRelation = "registros")
public record RegistroRequest(Long id,
                              @NotNull(message = "O campo 'hora' não pode ser nulo!")
                              @JsonFormat(pattern = PADRAO_ENTRADA_TEMPO, shape = JsonFormat.Shape.STRING)
                              LocalTime hora,
                              @NotBlank(message = "O campo 'sentido' não pode ser branco ou nulo!")
                              String sentido,
                              @NotNull(message = "O campo 'ativo' não pode ser nulo!")
                              Boolean ativo,
                              @JsonProperty(value = "codigo_acesso")
                              Integer codigoAcesso) {
    public Registro toDomain() {
        return Registro.builder()
                .id(id)
                .hora(hora)
                .sentido(Sentido.toEnum(sentido))
                .codigoAcesso(codigoAcesso)
                .ativo(ativo)
                .build();
    }
}
