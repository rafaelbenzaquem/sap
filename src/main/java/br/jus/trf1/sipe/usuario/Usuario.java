package br.jus.trf1.sipe.usuario;

import br.jus.trf1.sipe.usuario.web.dto.UsuarioResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios", schema = "sispontodb",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"matricula"}, name = "uk_usuario_matricula"),
        @UniqueConstraint(columnNames = {"cracha"}, name = "uk_usuario_cracha")
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 50)
    private String nome;
    @Column(unique = true, nullable = false, length = 15)
    private String matricula;
    @Column(unique = true, nullable = false, length = 20)
    private String cracha;
    @Min(value = 4)
    @Max(value = 12)
    @Column(name = "hora_diaria", nullable = false)
    private Integer horaDiaria;

    public UsuarioResponse toResponse() {
        return new UsuarioResponse(id, nome, matricula, cracha, horaDiaria);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Usuario usuario)) return false;

        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
