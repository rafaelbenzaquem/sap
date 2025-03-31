package br.jus.trf1.sap.registro.web.dto;

import br.jus.trf1.sap.registro.Registro;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalTime;

@Relation(collectionRelation = "registros")
public record RegistroResponse(Long id,
                               @JsonFormat(pattern = "HH:mm:ss", shape = JsonFormat.Shape.STRING)
                               LocalTime hora,
                               String sentido,
                               @JsonProperty("codigo_acesso")
                               Integer codigoAcesso,
                               @JsonProperty("versao")
                               Integer versao
) {
    public static RegistroResponse of(Registro registro) {
        return new RegistroResponse(
                registro.getId(),
                registro.getHora(),
                registro.getSentido().getPalavra(),
                registro.getCodigoAcesso(),
                registro.getVersao()
        );
    }
}
