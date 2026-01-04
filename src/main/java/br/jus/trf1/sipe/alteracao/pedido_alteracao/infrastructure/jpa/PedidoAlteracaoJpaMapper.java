package br.jus.trf1.sipe.alteracao.pedido_alteracao.infrastructure.jpa;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.AlteracaoRegistro;
import br.jus.trf1.sipe.alteracao.alteracao_registro.infrastructure.jpa.AlteracaoRegistroJpa;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.ponto.infrastructure.jpa.PontoJpa;
import br.jus.trf1.sipe.ponto.infrastructure.jpa.PontoJpaMapper;
import br.jus.trf1.sipe.usuario.infrastructure.jpa.UsuarioJpa;
import br.jus.trf1.sipe.usuario.infrastructure.jpa.UsuarioJpaMapper;

public class PedidoAlteracaoJpaMapper {
    public static PedidoAlteracaoJpa toEntity(PedidoAlteracao pedidoAlteracao) {
        return PedidoAlteracaoJpa.builder()
                .id(pedidoAlteracao.getId())
                .dataSolicitacao(pedidoAlteracao.getDataSolicitacao())
                .dataAprovacao(pedidoAlteracao.getDataAprovacao())
                .status(pedidoAlteracao.getStatus())
                .justificativa(pedidoAlteracao.getJustificativa())
                .justificativaAprovador(pedidoAlteracao.getJustificativaAprovador())
                .usuarioSolicitante(UsuarioJpa.builder()
                        .id(pedidoAlteracao.getUsuarioSolicitante().getId())
                        .build())
                .ponto(PontoJpa.builder()
                        .id(PontoJpaMapper.toEntityId(pedidoAlteracao.getPonto().getId()))
                        .build())
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
                .ponto(Ponto.builder()
                        .id(PontoJpaMapper.toDomainId(pedidoAlteracaoJpa.getPonto().getId()))
                        .build())
                .usuarioSolicitante(UsuarioJpaMapper.toDomain(pedidoAlteracaoJpa.getUsuarioSolicitante()))
                .alteracaoRegistros(pedidoAlteracaoJpa.getAlteracaoRegistros() == null ? null :
                        pedidoAlteracaoJpa.getAlteracaoRegistros().stream().map(ar -> AlteracaoRegistro.builder()
                                .id(ar.getId())
                                .build()).toList())

                .build();
    }
}
