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
    public AlteracaoRegistro salvarAlteracaoNoRegistroDePonto(long idPedidoAlteracao, Long idRegistroOriginal, long idRegistroNovo, Acao acao) {
        AlteracaoRegistro alteracaoRegistro = AlteracaoRegistro.builder()
                .peidoAlteracao(PedidoAlteracao.builder()
                        .id(idPedidoAlteracao)
                        .build())
                .registroOriginal(idRegistroOriginal == null ? null : Registro.builder()
                        .id(idRegistroOriginal)
                        .build())
                .registroNovo(Registro.builder()
                        .id(idRegistroNovo)
                        .build())
                .acao(acao)
                .build();

        return alteracaoRegistroPersistencePort.salva(alteracaoRegistro);
    }

    @Override
    public void apagar(Long id) {
        alteracaoRegistroPersistencePort.buscaPorId(id).ifPresent(alteracaoRegistroPersistencePort::apaga);
    }

    @Override
    public void apagarPorIdPedidoAlteracao(Long idPedidoAlteracao) {
        pedidoAlteracaoServicePort.buscaPedidoAlteracao(idPedidoAlteracao)
                .getAlteracaoRegistros()
                .forEach(alteracaoRegistroPersistencePort::apaga);

    }


}
