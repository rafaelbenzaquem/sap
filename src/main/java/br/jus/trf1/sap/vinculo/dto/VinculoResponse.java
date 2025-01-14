package br.jus.trf1.sap.vinculo.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record VinculoResponse (@JsonProperty("id")Integer id,
                              @JsonProperty("nome") String nome,
                              @JsonProperty("matricula") Integer matricula,
                              @JsonProperty("cracha")String cracha) {
}
