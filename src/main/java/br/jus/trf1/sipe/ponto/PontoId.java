package br.jus.trf1.sipe.ponto;

import jakarta.persistence.Embeddable;
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

    private String matricula;

    private LocalDate dia;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof PontoId pontoId)) return false;

        return Objects.equals(matricula, pontoId.matricula) && Objects.equals(dia, pontoId.dia);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(matricula);
        result = 31 * result + Objects.hashCode(dia);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "matricula=" + matricula +
                ", dia=" + paraString(dia, PADRAO_SAIDA_DATA) +
                '}';
    }
}
