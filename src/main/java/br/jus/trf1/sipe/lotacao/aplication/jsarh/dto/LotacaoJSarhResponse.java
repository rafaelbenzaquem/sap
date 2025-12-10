package br.jus.trf1.sipe.lotacao.aplication.jsarh.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;


@Builder
@AllArgsConstructor
@Getter
public class LotacaoJSarhResponse extends RepresentationModel<LotacaoJSarhResponse> {

    private final Integer id;
    private final String sigla;
    private final String descricao;
    @JsonProperty("lotacao_pai")
    private final Integer lotacaoPai;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof LotacaoJSarhResponse that)) return false;
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
