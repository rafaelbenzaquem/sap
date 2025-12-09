package br.jus.trf1.sipe.alteracao.pedido_alteracao;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.exceptions.PedidoAlteracaoInexistenteException;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.registro.Registro;
import br.jus.trf1.sipe.registro.RegistroRepository;
import br.jus.trf1.sipe.registro.exceptions.RegistroInexistenteException;
import br.jus.trf1.sipe.servidor.infrastructure.persistence.ServidorJpa;
import br.jus.trf1.sipe.usuario.infrastructure.persistence.UsuarioJpa;
import br.jus.trf1.sipe.usuario.domain.service.UsuarioService;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioNaoAprovadorException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PedidoAlteracaoService {

    private final PedidoAlteracaoRepository pedidoAlteracaoRepository;
    private final UsuarioService usuarioService;
    private final RegistroRepository registroRepository;


    public PedidoAlteracaoService(PedidoAlteracaoRepository pedidoAlteracaoRepository, UsuarioService usuarioService,
                                  RegistroRepository registroRepository) {
        this.pedidoAlteracaoRepository = pedidoAlteracaoRepository;
        this.usuarioService = usuarioService;
        this.registroRepository = registroRepository;
    }

    public PedidoAlteracao criarPedidoAlteracao(Ponto ponto, String justificativa, UsuarioJpa usuarioJpaSolicitante) {

        var pedidoAlteracao = PedidoAlteracao.builder()
                .ponto(ponto)
                .usuarioJpaSolicitante(usuarioJpaSolicitante)
                .status(StatusPedido.PENDENTE)
                .justificativa(justificativa)
                .build();
        return pedidoAlteracaoRepository.save(pedidoAlteracao);
    }

    public PedidoAlteracao atualizaPedidoAlteracao(PedidoAlteracao pedidoAlteracao) {
        var ponto = pedidoAlteracao.getPonto();
        usuarioService.temPermissaoRecurso(ponto);
        if (pedidoAlteracao.getStatus() == StatusPedido.APROVADO) {
            pedidoAlteracao.setDataAprovacao(LocalDateTime.now());
            pedidoAlteracao.getAlteracaoRegistros().forEach(alteracaoRegistro -> {
                var registroNovo = alteracaoRegistro.getRegistroNovo();
                if (registroNovo != null) {
                    aprovarRegistro(registroNovo.getId());
                }

            });

        }
        return pedidoAlteracaoRepository.save(pedidoAlteracao);
    }

    public Registro aprovarRegistro(Long idRegistro) {
        var registro = registroRepository.findById(idRegistro).orElseThrow(() -> new RegistroInexistenteException(idRegistro));
        var usuario = usuarioService.getUsuarioAutenticado();
        if (usuario instanceof ServidorJpa servidor) {
            registro.setServidorAprovador(servidor);
            registro.setDataAprovacao(Timestamp.valueOf(LocalDateTime.now()));
            return registroRepository.save(registro);
        }
        throw new UsuarioNaoAprovadorException(usuario.getMatricula());
    }

    public PedidoAlteracao buscaPedidoAlteracao(Long idPedidoAlteracao) {
        return pedidoAlteracaoRepository.buscaPedidoAlteracaoPorId(idPedidoAlteracao).orElseThrow(() -> new PedidoAlteracaoInexistenteException(idPedidoAlteracao));
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
