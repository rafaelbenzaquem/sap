package br.jus.trf1.sipe.ponto.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UsuariosPontoResponse(@JsonProperty("nome_usuario") String nomeUsuario,
                                    @JsonProperty("mensagem")String mensagem) {
}
