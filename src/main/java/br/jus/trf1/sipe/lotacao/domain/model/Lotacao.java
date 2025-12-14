package br.jus.trf1.sipe.lotacao.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lotacao{

    private Integer id;

    private String sigla;

    private String descricao;

    private Lotacao lotacaoPai;

    private List<Lotacao> subLotacoes;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Lotacao lotacao)) return false;

        return Objects.equals(id, lotacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
