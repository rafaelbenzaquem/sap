package br.jus.trf1.sap.vinculo.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NovoVinculoRequest(@NotBlank(message = "Campo não pode ser vazio ou em branco.") String nome,
                                 @NotNull(message = "Campo não pode ser nulo")Integer matricula,
                                 @NotBlank(message = "Campo não pode ser vazio ou em branco.")String cracha) {
}
