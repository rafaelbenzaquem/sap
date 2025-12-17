package br.jus.trf1.sipe.alteracao.pedido_alteracao.infrastructure.jpa;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.AlteracaoRegistro;
import br.jus.trf1.sipe.alteracao.alteracao_registro.infrastructure.jpa.AlteracaoRegistroJpa;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;

public class PedidoAlteracaoJpaMapper {
    public static PedidoAlteracaoJpa toEntity(PedidoAlteracao pedidoAlteracao) {
        return PedidoAlteracaoJpa.builder()
                .id(pedidoAlteracao.getId())
                .dataSolicitacao(pedidoAlteracao.getDataSolicitacao())
                .dataAprovacao(pedidoAlteracao.getDataAprovacao())
                .status(pedidoAlteracao.getStatus())
                .justificativa(pedidoAlteracao.getJustificativa())
                .justificativaAprovador(pedidoAlteracao.getJustificativaAprovador())
                .alteracaoRegistros(pedidoAlteracao.getAlteracaoRegistros() == null ? null :
                        pedidoAlteracao.getAlteracaoRegistros().stream().map(ar -> AlteracaoRegistroJpa.builder()
                                .id(ar.getId())
                                .build()).toList())
                .build();
    }

    public static PedidoAlteracao toDomain(PedidoAlteracaoJpa pedidoAlteracaoJpa) {
        return PedidoAlteracao.builder()
                .id(pedidoAlteracaoJpa.getId())
                .dataSolicitacao(pedidoAlteracaoJpa.getDataSolicitacao())
                .dataAprovacao(pedidoAlteracaoJpa.getDataAprovacao())
                .status(pedidoAlteracaoJpa.getStatus())
                .justificativa(pedidoAlteracaoJpa.getJustificativa())
                .justificativaAprovador(pedidoAlteracaoJpa.getJustificativaAprovador())
                .alteracaoRegistros(pedidoAlteracaoJpa.getAlteracaoRegistros() == null ? null :
                        pedidoAlteracaoJpa.getAlteracaoRegistros().stream().map(ar -> AlteracaoRegistro.builder()
                                .id(ar.getId())
                                .build()).toList())

                .build();
    }
}
