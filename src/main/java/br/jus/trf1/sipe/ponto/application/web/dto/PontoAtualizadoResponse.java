package br.jus.trf1.sipe.ponto.application.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_SAIDA_DATA;

@Relation(collectionRelation = "pontos")
public record PontoAtualizadoResponse(String matricula,
                                      @JsonFormat(pattern = PADRAO_SAIDA_DATA, shape = JsonFormat.Shape.STRING)
                                      LocalDate dia,
                                      String descricao,
                                      Float indice,
                                      @JsonProperty("total_segundos")
                                      Long totalSegundos,
                                      @JsonProperty("pedido_alteracao_pendente")
                                      Boolean pedidoAlteracaoPendente) {

}
