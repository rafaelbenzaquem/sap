package br.jus.trf1.sipe.alteracao.pedido_alteracao.web.dto;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracao;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Objects;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_SAIDA_DATA;

@Builder
public record PedidoAlteracaoResponse(Long id,
                                      @JsonProperty("matricula_ponto")
                                      String matriculaPonto,
                                      @DateTimeFormat(pattern = PADRAO_SAIDA_DATA)
                                      @JsonProperty("dia_ponto")
                                      LocalDate diaPonto,
                                      String status,
                                      String justificativa) {


    public static PedidoAlteracaoResponse of(PedidoAlteracao pedidoAlteracao) {
        Objects.requireNonNull(pedidoAlteracao);
        Objects.requireNonNull(pedidoAlteracao.getPonto());

        return PedidoAlteracaoResponse.builder()
                .id(pedidoAlteracao.getId())
                .matriculaPonto(pedidoAlteracao.getPonto().getId().getMatricula())
                .diaPonto(pedidoAlteracao.getPonto().getId().getDia())
                .status(pedidoAlteracao.getStatus().name())
                .justificativa(pedidoAlteracao.getJustificativa())
                .build();

    }
}
