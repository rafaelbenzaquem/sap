package br.jus.trf1.sipe.usuario.infrastructure.persistence;

import br.jus.trf1.sipe.ausencia.Ausencia;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.usuario.application.web.dto.UsuarioResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "usuarios", schema = "sispontodb",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"matricula"}, name = "uk_usuario_matricula"),
        @UniqueConstraint(columnNames = {"cracha"}, name = "uk_usuario_cracha")
})
public class UsuarioJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 50)
    private String nome;
    @Column(unique = true, nullable = false, length = 15)
    private String matricula;
    @Column(unique = true, nullable = false, length = 20)
    private Integer cracha;
    @Min(value = 4)
    @Max(value = 12)
    @Column(name = "hora_diaria", nullable = false)
    private Integer horaDiaria;


    @OneToMany(mappedBy = "id.usuarioJPA", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ponto> pontos;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ausencia> ausencias;

    public UsuarioJpa(Integer id, String nome, String matricula, Integer cracha, Integer horaDiaria) {
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
        if (!(o instanceof UsuarioJpa usuarioJPA)) return false;

        return Objects.equals(id, usuarioJPA.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UsuarioJpa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", matricula='" + matricula + '\'' +
                ", cracha='" + cracha + '\'' +
                ", horaDiaria=" + horaDiaria +
                '}';
    }
}
