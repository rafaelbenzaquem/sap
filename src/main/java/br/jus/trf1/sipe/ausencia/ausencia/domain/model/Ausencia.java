package br.jus.trf1.sipe.ausencia.ausencia.domain.model;

import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Objects;

@SuperBuilder
@NoArgsConstructor
@Setter
@Getter
public class Ausencia {

    private String id;
    private LocalDate inicio;
    private LocalDate fim;
    private String descricao;

    private Usuario usuario;

    public Ausencia(String id, LocalDate inicio, LocalDate fim, String descricao, Usuario usuario) {
        this.id = id;
        this.inicio = inicio;
        this.fim = fim;
        this.descricao = descricao;
        this.usuario = usuario;
    }

    public Ausencia(Ausencia ausencia) {
        this.id = ausencia.getId();
        this.inicio = ausencia.getInicio();
        this.fim = ausencia.getFim();
        this.descricao = ausencia.getDescricao();
        this.usuario = ausencia.getUsuario();
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Ausencia ausencia)) return false;

        return Objects.equals(id, ausencia.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Ausencia{" +
                "id=" + id +
                ", inicio=" + inicio +
                ", fim=" + fim +
                ", descricao='" + descricao + '\'' +
                ", usuario=" + usuario +
                '}';
    }
}
