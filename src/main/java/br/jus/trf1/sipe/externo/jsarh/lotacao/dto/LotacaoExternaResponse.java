package br.jus.trf1.sipe.externo.jsarh.lotacao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;


@Builder
@AllArgsConstructor
@Getter
public class LotacaoExternaResponse extends RepresentationModel<LotacaoExternaResponse> {

    private final Integer id;
    private final String sigla;
    private final String descricao;
    @JsonProperty("lotacao_pai")
    private final Integer lotacaoPai;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof LotacaoExternaResponse that)) return false;
        if (!super.equals(o)) return false;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(id);
        return result;
    }
}
