package br.jus.trf1.sipe.usuario.application.web.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioAtualizadoRequest(@NotBlank(message = "Campo 'nome' não pode ser vazio ou em branco.")
                                       String nome,
                                       @NotBlank(message = "Campo 'matricula' não pode ser vazio ou em branco.")
                                       String matricula,
                                       @NotBlank(message = "Campo 'cracha' não pode ser vazio ou em branco.")
                                       String cracha,
                                       @NotNull(message = "Campo 'hora_diaria' não pode ser ")
                                       @JsonProperty("hora_diaria")
                                       Integer horaDiaria) {
}
