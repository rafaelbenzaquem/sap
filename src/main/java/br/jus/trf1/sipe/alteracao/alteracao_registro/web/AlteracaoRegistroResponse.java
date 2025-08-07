package br.jus.trf1.sipe.alteracao.alteracao_registro.web;

import br.jus.trf1.sipe.alteracao.alteracao_registro.AlteracaoRegistro;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.hateoas.server.core.Relation;

import java.util.Objects;

@Builder
@Relation(collectionRelation = "alteracoes")
public record AlteracaoRegistroResponse(@JsonProperty("id_registro_original") Long idRegistroOriginal,
                                        @JsonProperty("id_registro_novo") Long idRegistroNovo,
                                        String acao) {


    public static AlteracaoRegistroResponse from(AlteracaoRegistro registro) {
        Objects.requireNonNull(registro);
        return AlteracaoRegistroResponse.builder()
                .idRegistroNovo(registro.getRegistroNovo() == null ? null : registro.getRegistroNovo().getId())
                .idRegistroOriginal(registro.getRegistroOriginal() == null ? null :registro.getRegistroOriginal().getId())
                .acao(registro.getAcao().name())
                .build();
    }

}
