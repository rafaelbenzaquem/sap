package br.jus.trf1.sipe.ponto;

import br.jus.trf1.sipe.usuario.Usuario;
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
public class PontoId {


    @OneToOne
    @JoinColumns(value = {
            @JoinColumn(name = "matricula", referencedColumnName = "matricula")
    }, foreignKey = @ForeignKey(name = "fk_servidor_ponto"))
    private Usuario usuario;

    private LocalDate dia;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof PontoId pontoId)) return false;

        return Objects.equals(usuario.getMatricula(), pontoId.getUsuario().getMatricula()) && Objects.equals(dia, pontoId.dia);
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
