package br.jus.trf1.sipe.alteracao.pedido_alteracao;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.exceptions.PedidoAlteracaoInexistenteException;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.usuario.Usuario;
import br.jus.trf1.sipe.usuario.UsuarioAtualService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PedidoAlteracaoService {

    private final PedidoAlteracaoRepository pedidoAlteracaoRepository;
    private final UsuarioAtualService usuarioAtualService;


    public PedidoAlteracaoService(PedidoAlteracaoRepository pedidoAlteracaoRepository, UsuarioAtualService usuarioAtualService) {
        this.pedidoAlteracaoRepository = pedidoAlteracaoRepository;
        this.usuarioAtualService = usuarioAtualService;
    }

    public PedidoAlteracao criarPedidoAlteracao(Ponto ponto, String justificativa, Usuario usuarioSolicitante) {

        var pedidoAlteracao = PedidoAlteracao.builder()
                .ponto(ponto)
                .usuarioSolicitante(usuarioSolicitante)
                .status(StatusPedido.PENDENTE)
                .justificativa(justificativa)
                .build();
        return pedidoAlteracaoRepository.save(pedidoAlteracao);
    }

    public PedidoAlteracao buscaPedidoAlteracao(Long idPedidoAlteracao) {
        return pedidoAlteracaoRepository.findById(idPedidoAlteracao).orElseThrow(() -> new PedidoAlteracaoInexistenteException(idPedidoAlteracao));
    }

    public PedidoAlteracao apagar(long idPedido) {

        var pedidoAlteracao = buscaPedidoAlteracao(idPedido);

        pedidoAlteracaoRepository.delete(pedidoAlteracao);

        return pedidoAlteracao;
    }
}
