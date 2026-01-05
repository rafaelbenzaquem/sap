package br.jus.trf1.sipe.alteracao.alteracao_registro.domain.port.out;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.Acao;
import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.AlteracaoRegistro;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
import br.jus.trf1.sipe.registro.domain.model.Registro;

import java.util.Optional;

public interface AlteracaoRegistroPersistencePort {

    AlteracaoRegistro salva(AlteracaoRegistro alteracaoRegistro);

    Optional<AlteracaoRegistro> buscaPorId(Long id);

    void apaga(AlteracaoRegistro alteracaoRegistro);

    AlteracaoRegistro salvaAlteracaoDeRegistroDePonto(PedidoAlteracao pedidoAlteracao, Registro registroOriginal, Registro registroNovo, Acao acao);
}
