package br.jus.trf1.sipe.alteracao.alteracao_registro.domain.service;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.Acao;
import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.AlteracaoRegistro;
import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.port.in.AlteracaoRegistroServicePort;
import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.port.out.AlteracaoRegistroPersistencePort;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.port.in.PedidoAlteracaoServicePort;
import br.jus.trf1.sipe.registro.domain.model.Registro;
import org.springframework.stereotype.Service;

@Service
public class AlteracaoRegistroService implements AlteracaoRegistroServicePort {

    private final AlteracaoRegistroPersistencePort alteracaoRegistroPersistencePort;
    private final PedidoAlteracaoServicePort pedidoAlteracaoServicePort;


    public AlteracaoRegistroService(AlteracaoRegistroPersistencePort alteracaoRegistroPersistencePort,
                                    PedidoAlteracaoServicePort pedidoAlteracaoServicePort) {
        this.alteracaoRegistroPersistencePort = alteracaoRegistroPersistencePort;
        this.pedidoAlteracaoServicePort = pedidoAlteracaoServicePort;
    }

    @Override
    public AlteracaoRegistro salvaAlteracaoNoRegistroDePonto(PedidoAlteracao pedidoAlteracao, Registro registroOriginal, Registro registroNovo, Acao acao) {
        return alteracaoRegistroPersistencePort.salvaAlteracaoDeRegistroDePonto(pedidoAlteracao,registroOriginal,registroNovo,acao);
    }

    @Override
    public void  apagar(Long id) {
        alteracaoRegistroPersistencePort.buscaPorId(id).ifPresent(alteracaoRegistroPersistencePort::apaga);
    }

    @Override
    public void apagarPorIdPedidoAlteracao(Long idPedidoAlteracao) {
        pedidoAlteracaoServicePort.buscaPedidoAlteracao(idPedidoAlteracao)
                .getAlteracoesRegistros()
                .forEach(alteracaoRegistroPersistencePort::apaga);

    }


}
