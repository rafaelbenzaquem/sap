package br.jus.trf1.sap.ponto;

import br.jus.trf1.sap.util.DataTempoUtil;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

import static br.jus.trf1.sap.util.ConstantesDataTempoUtil.PADRAO_SAIDA_DATA;

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

    /**
     * @return String de PontoId no formato Json
     */
    @Override
    public String toString() {
        return """
                {
                    "matricula": %s,
                    "dia" %s
                }
                """.formatted(matricula, DataTempoUtil.paraStringData(dia, PADRAO_SAIDA_DATA));
    }
}
