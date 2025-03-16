package br.jus.trf1.sap.vinculo.web.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VinculoAtualizadoRequest(@NotNull(message = "Campo não pode ser nulo") Integer id,
                                       @NotBlank(message = "Campo não pode ser vazio ou em branco.") String nome,
                                       @NotBlank(message = "Campo não pode ser vazio ou em branco.")String matricula,
                                       @NotBlank(message = "Campo não pode ser vazio ou em branco.")String cracha) {
}
