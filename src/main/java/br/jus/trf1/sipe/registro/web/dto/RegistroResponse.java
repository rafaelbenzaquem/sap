package br.jus.trf1.sipe.registro.web.dto;

import br.jus.trf1.sipe.registro.Registro;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalTime;

@Builder
@Relation(collectionRelation = "registros")
public record RegistroResponse(Long id,
                               @JsonFormat(pattern = "HH:mm:ss", shape = JsonFormat.Shape.STRING)
                               LocalTime hora,
                               String sentido,
                               Boolean ativo,
                               @JsonProperty("codigo_acesso")
                               Integer codigoAcesso
) {
    public static RegistroResponse of(Registro registro) {
        return RegistroResponse.builder()
                .id(registro.getId())
                .hora(registro.getHora())
                .sentido(registro.getSentido().getPalavra())
                .ativo(registro.getAtivo())
                .codigoAcesso(registro.getCodigoAcesso())
                .build();
    }
}
