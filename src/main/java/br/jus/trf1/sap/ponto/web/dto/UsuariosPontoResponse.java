package br.jus.trf1.sap.ponto.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UsuariosPontoResponse(@JsonProperty("nome_usuario") String nomeUsuario,
                                    @JsonProperty("mensagem")String mensagem) {
}
