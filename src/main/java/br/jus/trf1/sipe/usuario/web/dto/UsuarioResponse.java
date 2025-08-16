package br.jus.trf1.sipe.usuario.web.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "usuarios")
public record UsuarioResponse(@JsonProperty("id") Integer id,
                              @JsonProperty("nome") String nome,
                              @JsonProperty("matricula") String matricula,
                              @JsonProperty("cracha") Integer cracha,
                              @JsonProperty("hora_diaria") Integer horaDiaria) {
}
