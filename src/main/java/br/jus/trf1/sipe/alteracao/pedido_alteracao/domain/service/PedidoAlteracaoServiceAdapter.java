package br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.service;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.StatusPedido;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.port.in.PedidoAlteracaoServicePort;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.port.out.PedidoAlteracaoPersistencePort;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.exceptions.PedidoAlteracaoInexistenteException;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.domain.port.in.UsuarioServicePort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PedidoAlteracaoServiceAdapter implements PedidoAlteracaoServicePort {

    private final PedidoAlteracaoPersistencePort pedidoAlteracaoPersistencePort;
    private final UsuarioServicePort usuarioServicePort;

    public PedidoAlteracaoServiceAdapter(PedidoAlteracaoPersistencePort pedidoAlteracaoPersistencePort,
                                         UsuarioServicePort usuarioServicePort) {
        this.pedidoAlteracaoPersistencePort = pedidoAlteracaoPersistencePort;
        this.usuarioServicePort = usuarioServicePort;
    }

    @Override
    public PedidoAlteracao criarPedidoAlteracao(Ponto ponto, String justificativa, Usuario usuarioSolicitante) {
        var pedidoAlteracao = PedidoAlteracao.builder()
                .ponto(ponto)
                .usuarioSolicitante(usuarioSolicitante)
                .status(StatusPedido.PENDENTE)
                .justificativa(justificativa)
                .build();
        return pedidoAlteracaoPersistencePort.salva(pedidoAlteracao);
    }

    @Override
    public PedidoAlteracao atualizaPedidoAlteracao(PedidoAlteracao pedidoAlteracao) {
        var ponto = pedidoAlteracao.getPonto();
        usuarioServicePort.temPermissaoRecurso(ponto);
//        if (pedidoAlteracao.getStatus() == StatusPedido.APROVADO) {
//            pedidoAlteracao.setDataAprovacao(LocalDateTime.now());
//            pedidoAlteracao.getAlteracaoRegistros().forEach(alteracaoRegistro -> {
//                var registroNovo = alteracaoRegistro.getRegistroNovo();
//                if (registroNovo != null) {
//                    aprovarRegistro(registroNovo.getId());
//                }
//
//            });
//
//        }
        return pedidoAlteracaoPersistencePort.salva(pedidoAlteracao);
    }

    //TODO mover para o domínio de regisgtro
//    @Override
//    public RegistroJpa aprovarRegistro(Long idRegistro) {
//        var registro = registroRepository.findById(idRegistro).orElseThrow(() -> new RegistroInexistenteException(idRegistro));
//        var usuario = usuarioServicePort.getUsuarioAutenticado();
//        var usuarioJpa = UsuarioJpaMapper.toEntity(usuario);
//        if (usuarioJpa instanceof ServidorJpa servidor) {
//            registro.setServidorAprovador(servidor);
//            registro.setDataAprovacao(Timestamp.valueOf(LocalDateTime.now()));
//            return registroRepository.save(registro);
//        }
//        throw new UsuarioNaoAprovadorException(usuario.getMatricula());
//    }

    @Override
    public PedidoAlteracao buscaPedidoAlteracao(Long idPedidoAlteracao) {
        return pedidoAlteracaoPersistencePort.buscaPorId(idPedidoAlteracao).orElseThrow(() -> new PedidoAlteracaoInexistenteException(idPedidoAlteracao));
    }

    @Override
    public Optional<PedidoAlteracao> buscaPedidoAlteracao(String matricula, LocalDate dia) {
        return pedidoAlteracaoPersistencePort.buscaPorPonto(matricula, dia, true);
    }

    @Override
    public PedidoAlteracao apagar(long idPedidoAlteracao) {

        var pedidoAlteracao = buscaPedidoAlteracao(idPedidoAlteracao);

        pedidoAlteracaoPersistencePort.apagarPorId(idPedidoAlteracao);

        return pedidoAlteracao;
    }

}
