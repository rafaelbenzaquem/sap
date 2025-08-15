package br.jus.trf1.sipe.alteracao.pedido_alteracao;

import br.jus.trf1.sipe.alteracao.alteracao_registro.AlteracaoRegistro;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.exceptions.PedidoAlteracaoInexistenteException;
import br.jus.trf1.sipe.comum.util.DataTempoUtil;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.usuario.Usuario;
import br.jus.trf1.sipe.usuario.UsuarioAtualService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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

    public PedidoAlteracao atualizaPedidoAlteracao(PedidoAlteracao pedidoAlteracao) {
        var ponto = pedidoAlteracao.getPonto();
        usuarioAtualService.permissoesNivelUsuario(ponto.getId().getMatricula());
        return pedidoAlteracaoRepository.save(pedidoAlteracao);
    }

    public PedidoAlteracao buscaPedidoAlteracao(Long idPedidoAlteracao) {
        return pedidoAlteracaoRepository.findById(idPedidoAlteracao).orElseThrow(() -> new PedidoAlteracaoInexistenteException(idPedidoAlteracao));
    }

    public Optional<PedidoAlteracao> buscaPedidoAlteracao(String matricula, LocalDate dia) {
        return pedidoAlteracaoRepository.buscaPorPontoEmAprovacao(matricula, dia);
    }

    public PedidoAlteracao apagar(long idPedidoAlteracao) {

        var pedidoAlteracao = buscaPedidoAlteracao(idPedidoAlteracao);

        pedidoAlteracaoRepository.apagarPedidoAlteracaoPorId(idPedidoAlteracao);

        return pedidoAlteracao;
    }

}
