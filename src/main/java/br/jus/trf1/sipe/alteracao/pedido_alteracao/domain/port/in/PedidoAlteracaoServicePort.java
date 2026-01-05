package br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.port.in;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;

import java.time.LocalDate;
import java.util.Optional;

public interface PedidoAlteracaoServicePort {

    PedidoAlteracao criarPedidoAlteracao(Ponto ponto, String justificativa, Usuario usuarioSolicitante);

    PedidoAlteracao atualizaPedidoAlteracao(PedidoAlteracao pedidoAlteracao);

    PedidoAlteracao buscaPedidoAlteracao(Long idPedidoAlteracao);

    Optional<PedidoAlteracao> buscaPedidoAlteracao(String matricula, LocalDate dia);

    PedidoAlteracao apagar(long idPedidoAlteracao);
}
