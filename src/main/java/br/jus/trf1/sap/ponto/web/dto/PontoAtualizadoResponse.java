package br.jus.trf1.sap.ponto.web.dto;

import br.jus.trf1.sap.ponto.Ponto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

import static br.jus.trf1.sap.comum.util.ConstantesDataTempoUtil.PADRAO_SAIDA_DATA;

@Relation(collectionRelation = "pontos")
public record PontoAtualizadoResponse(String matricula,
                                      @JsonFormat(pattern = PADRAO_SAIDA_DATA, shape = JsonFormat.Shape.STRING)
                                      LocalDate dia,
                                      String descricao,
                                      Float indice,
                                      @JsonProperty("total_segundos")
                                      Long totalSegundos) {
    public static PontoAtualizadoResponse of(Ponto ponto) {
        return new PontoAtualizadoResponse(ponto.getId().getMatricula(), ponto.getId().getDia(),
                ponto.getDescricao(), ponto.getIndice().getValor(), ponto.getHorasPermanencia().toSeconds());
    }
}
