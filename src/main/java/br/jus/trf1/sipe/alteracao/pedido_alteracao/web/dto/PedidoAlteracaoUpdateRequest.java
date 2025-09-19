package br.jus.trf1.sipe.alteracao.pedido_alteracao.web.dto;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracao;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Objects;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;

@Builder
public record PedidoAlteracaoUpdateRequest(
        Long id,
        @NotBlank(message = "O campo 'matricula_ponto' não pode ser branco ou nulo!")
        @JsonProperty("matricula_ponto")
        String matriculaPonto,
        @NotNull(message = "O campo 'dia_ponto' não pode ser nulo!")
        @JsonFormat(pattern = PADRAO_ENTRADA_DATA, shape = JsonFormat.Shape.STRING)
        @JsonProperty("dia_ponto")
        LocalDate diaPonto,
        String status,
        String justificativa,
        @JsonProperty("justificativa_aprovador")
        String justificativaAprovador) {



    public static PedidoAlteracaoUpdateRequest of(PedidoAlteracao pedidoAlteracao) {
        Objects.requireNonNull(pedidoAlteracao);
        Objects.requireNonNull(pedidoAlteracao.getPonto());

        return PedidoAlteracaoUpdateRequest.builder()
                .id(pedidoAlteracao.getId())
                .matriculaPonto(pedidoAlteracao.getPonto().getId().getUsuario().getMatricula())
                .diaPonto(pedidoAlteracao.getPonto().getId().getDia())
                .status(pedidoAlteracao.getStatus().name())
                .justificativa(pedidoAlteracao.getJustificativa())
                .build();

    }
}
