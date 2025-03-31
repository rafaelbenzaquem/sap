package br.jus.trf1.sap.vinculo.web.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "vinculos")
public record VinculoResponse(@JsonProperty("id") Integer id,
                              @JsonProperty("nome") String nome,
                              @JsonProperty("matricula") String matricula,
                              @JsonProperty("cracha") String cracha,
                              @JsonProperty("hora_diaria") Integer horaDiaria) {
}
