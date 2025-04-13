package br.jus.trf1.sipe.usuario.web.dto;


import jakarta.validation.constraints.NotBlank;

public record UsuarioAtualizadoRequest(@NotBlank(message = "Campo não pode ser vazio ou em branco.") String nome,
                                       @NotBlank(message = "Campo não pode ser vazio ou em branco.")String matricula,
                                       @NotBlank(message = "Campo não pode ser vazio ou em branco.")String cracha) {
}
