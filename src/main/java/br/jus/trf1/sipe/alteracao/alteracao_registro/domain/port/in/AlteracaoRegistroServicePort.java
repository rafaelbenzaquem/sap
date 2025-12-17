package br.jus.trf1.sipe.alteracao.alteracao_registro.domain.port.in;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.Acao;
import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.AlteracaoRegistro;

public interface AlteracaoRegistroServicePort {

    AlteracaoRegistro salvarAlteracaoNoRegistroDePonto(long idPedidoAlteracao, Long idRegistroOriginal, long idRegistroNovo, Acao acao);

    void apagar(Long id);

    void apagarPorIdPedidoAlteracao(Long idPedidoAlteracao);
}
