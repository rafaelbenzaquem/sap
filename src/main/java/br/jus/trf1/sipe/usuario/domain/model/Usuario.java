package br.jus.trf1.sipe.usuario.domain.model;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.usuario.application.web.dto.UsuarioResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Integer id;
    private String nome;
    private String matricula;
    private Integer cracha;
    private Integer horaDiaria;
    private List<Ponto> pontos;
    private List<Ausencia> ausencias;

    public Usuario(Integer id, String nome, String matricula, Integer cracha, Integer horaDiaria) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.cracha = cracha;
        this.horaDiaria = horaDiaria;
    }

    public UsuarioResponse toResponse() {
        return new UsuarioResponse(id, nome, matricula, cracha, horaDiaria);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Usuario usuarioJPA)) return false;

        return Objects.equals(id, usuarioJPA.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", matricula='" + matricula + '\'' +
                ", cracha='" + cracha + '\'' +
                ", horaDiaria=" + horaDiaria +
                '}';
    }
}
