package br.jus.trf1.sipe.alteracao.alteracao_registro.domain.port.in;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.Acao;
import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.AlteracaoRegistro;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
import br.jus.trf1.sipe.registro.domain.model.Registro;

public interface AlteracaoRegistroServicePort {

    AlteracaoRegistro salvaAlteracaoNoRegistroDePonto(PedidoAlteracao pedidoAlteracao, Registro registroOriginal, Registro registroNovo, Acao acao);

    void apagar(Long id);

    void apagarPorIdPedidoAlteracao(Long idPedidoAlteracao);
}
