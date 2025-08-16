package br.jus.trf1.sipe.alteracao.pedido_alteracao.exceptions;

public class PedidoAlteracaoInexistenteException extends RuntimeException {
    public PedidoAlteracaoInexistenteException(Long idPedidoAlteracao) {
        this("Não existe pedido de alteração com id: " + idPedidoAlteracao);
    }

    public PedidoAlteracaoInexistenteException(String message) {
        super(message);
    }
}
