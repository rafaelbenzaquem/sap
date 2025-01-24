package br.jus.trf1.sap.ponto;

import br.jus.trf1.sap.ponto.registro.Registro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ponto {

    @EmbeddedId
    private PontoId id;

    private String descricao;

    @OneToMany(mappedBy = "ponto", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Registro> registros;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Ponto ponto)) return false;

        return Objects.equals(id, ponto.id) && Objects.equals(descricao, ponto.descricao) && Objects.equals(registros, ponto.registros);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(descricao);
        result = 31 * result + Objects.hashCode(registros);
        return result;
    }
}
