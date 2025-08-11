package br.jus.trf1.sipe.usuario.web.dto;


import br.jus.trf1.sipe.comum.validadores.Unico;
import br.jus.trf1.sipe.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioNovoRequest(@NotBlank(message = "Campo 'nome' não pode ser vazio ou em branco.")
                                 String nome,
                                 @NotBlank(message = "Campo 'matricula' não pode ser vazio ou em branco.")
                                 @Unico(message = "Esta matrícula já foi cadastrada no banco de dados!",
                                         domainClass = Usuario.class,
                                         fieldName = "matricula") String matricula,
                                 @NotNull(message = "Campo 'cracha' não pode ser nulo.")
                                 @Unico(message = "Este crachá já foi cadastrado no banco de dados!",
                                         domainClass = Usuario.class,
                                         fieldName = "cracha")
                                 Integer cracha,
                                 @JsonProperty(value = "hora_diaria")
                                 @NotNull(message = "Campo 'hora_diaria' não pode ser nulo.")
                                 Integer horaDiaria) {
    public Usuario paraEntidade() {
        return new Usuario(null, this.nome(), this.matricula(), this.cracha(), this.horaDiaria());
    }
}
