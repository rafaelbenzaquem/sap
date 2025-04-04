package br.jus.trf1.sap.vinculo.web.dto;


import br.jus.trf1.sap.comum.validadores.Unico;
import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.vinculo.Vinculo;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VinculoNovoRequest(@NotBlank(message = "Campo 'nome' não pode ser vazio ou em branco.")
                                 String nome,
                                 @NotBlank(message = "Campo 'matricula' não pode ser vazio ou em branco.")
                                 @Unico(message = "Esta matrícula já foi cadastrada no banco de dados!",
                                         domainClass = Ponto.class,
                                         fieldName = "matricula") String matricula,
                                 @NotBlank(message = "Campo 'cracha' não pode ser vazio ou em branco.")
                                 @Unico(message = "Este crachá já foi cadastrado no banco de dados!",
                                         domainClass = Ponto.class,
                                         fieldName = "cracha")
                                 String cracha,
                                 @JsonProperty(value = "hora_diaria")
                                 @NotNull(message = "Campo 'hora_diaria' não pode ser nulo.")
                                 Integer horaDiaria) {
    public Vinculo paraEntidade() {
        return new Vinculo(null, this.nome(), this.matricula(), this.cracha(), this.horaDiaria());
    }
}
