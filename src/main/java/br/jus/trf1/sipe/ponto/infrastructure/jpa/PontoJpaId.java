package br.jus.trf1.sipe.ponto.infrastructure.jpa;

import br.jus.trf1.sipe.usuario.infrastructure.jpa.UsuarioJpa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_SAIDA_DATA;
import static br.jus.trf1.sipe.comum.util.DataTempoUtil.paraString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class PontoJpaId {


    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "matricula", referencedColumnName = "matricula")
    }, foreignKey = @ForeignKey(name = "fk_servidor_ponto"))
    private UsuarioJpa usuario;

    private LocalDate dia;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof PontoJpaId pontoJpaId)) return false;

        return Objects.equals(usuario.getMatricula(), pontoJpaId.getUsuario().getMatricula()) && Objects.equals(dia, pontoJpaId.dia);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(this.usuario.getMatricula());
        result = 31 * result + Objects.hashCode(dia);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "matricula=" + usuario.getMatricula() +
                ", dia=" + paraString(dia, PADRAO_SAIDA_DATA) +
                '}';
    }
}
