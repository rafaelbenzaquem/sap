package br.jus.trf1.sipe.alteracao.pedido_alteracao;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.usuario.Usuario;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PedidoAlteracaoService {

    private PedidoAlteracaoRepository pedidoAlteracaoRepository;


    public PedidoAlteracaoService(PedidoAlteracaoRepository pedidoAlteracaoRepository) {
        this.pedidoAlteracaoRepository = pedidoAlteracaoRepository;
    }

    public PedidoAlteracao criarPedidoAlteracao(Ponto ponto, String justificativa, Usuario usuarioSolicitante) {
        var pedidoAlteracao = PedidoAlteracao.builder()
                .ponto(ponto)
                .usuarioSolicitante(usuarioSolicitante)
                .dataCadastro(LocalDateTime.now())
                .status(StatusPedido.PENDENTE)
                .justificativa(justificativa)
                .build();
        return pedidoAlteracaoRepository.save(pedidoAlteracao);
    }

}
