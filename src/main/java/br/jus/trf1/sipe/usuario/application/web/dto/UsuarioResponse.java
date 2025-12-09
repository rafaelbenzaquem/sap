package br.jus.trf1.sipe.usuario.application.web.dto;

import lombok.Builder;

@Builder
public record UsuarioResponse(Integer id, String nome, String matricula, Integer cracha, Integer horaDiaria) {
}