package br.jus.trf1.sap.usuarios;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "usuario")
public class Usuario{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "lotacao")
    private String lotacao;

    @Column(name = "password")
    private String password;

    @Column(name = "criado_em")
    private Timestamp criadoEm;

    @Column(name = "atualiado_em")
    private Timestamp atualizadoEm;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    private Perfil perfil;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Double.compare(id, usuario.id) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", lotacao='" + lotacao + '\'' +
                ", password='" + password + '\'' +
                ", criadoEm=" + criadoEm +
                ", atualizadoEm=" + atualizadoEm +
                ", email='" + email + '\'' +
                ", perfil=" + perfil +
                '}';
    }
}
