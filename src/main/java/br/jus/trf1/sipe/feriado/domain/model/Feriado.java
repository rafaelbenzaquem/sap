package br.jus.trf1.sipe.feriado.domain.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Objects;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class  Feriado {

    private LocalDate data;

    private String descricao;

    private Integer abrangencia;

    private Integer tipo;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Feriado feriado)) return false;

        return Objects.equals(data, feriado.data) && Objects.equals(descricao, feriado.descricao) && Objects.equals(abrangencia, feriado.abrangencia) && Objects.equals(tipo, feriado.tipo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(data);
        result = 31 * result + Objects.hashCode(descricao);
        result = 31 * result + Objects.hashCode(abrangencia);
        result = 31 * result + Objects.hashCode(tipo);
        return result;
    }
}
