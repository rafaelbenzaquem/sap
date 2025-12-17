package br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.port.out;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;

import java.time.LocalDate;
import java.util.Optional;

public interface PedidoAlteracaoPersistencePort {

    PedidoAlteracao salva(PedidoAlteracao pedidoAlteracao);

    Optional<PedidoAlteracao> buscaPorId(Long idPedidoAlteracao);

    Optional<PedidoAlteracao> buscaPorPonto(String matricula, LocalDate dia, boolean emAprovacao);

    void apagarPorId(long idPedidoAlteracao);
}
