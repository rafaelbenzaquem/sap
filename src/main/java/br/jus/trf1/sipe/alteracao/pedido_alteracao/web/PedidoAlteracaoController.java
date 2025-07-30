package br.jus.trf1.sipe.alteracao.pedido_alteracao.web;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracaoService;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.web.dto.PedidoAlteracaoRequest;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.web.dto.PedidoAlteracaoResponse;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.usuario.UsuarioAtualService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/sipe/pedido/alteracao")
public class PedidoAlteracaoController {

    private final PedidoAlteracaoService pedidoAlteracaoService;
    private final UsuarioAtualService usuarioAtualService;
    private final PontoService pontoService;

    public PedidoAlteracaoController(PedidoAlteracaoService pedidoAlteracaoService, UsuarioAtualService usuarioAtualService, PontoService pontoService) {
        this.pedidoAlteracaoService = pedidoAlteracaoService;
        this.usuarioAtualService = usuarioAtualService;
        this.pontoService = pontoService;
    }


    @PostMapping
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<PedidoAlteracaoResponse> realizarPedido(@Valid @RequestBody PedidoAlteracaoRequest pedidoAlteracaoRequest) {

        var matriculaPonto = pedidoAlteracaoRequest.matriculaPonto();
        var diaPonto = pedidoAlteracaoRequest.diaPonto();
        var justificativa = pedidoAlteracaoRequest.justificativa();

        log.info("Atualizando realizando Pedido de Alteracao de Ponto - {} - {}", matriculaPonto, diaPonto);

        var usuario = usuarioAtualService.getUsuario();
        var ponto = pontoService.buscaPonto(matriculaPonto, diaPonto);
        var pedidoAlteracao = pedidoAlteracaoService.criarPedidoAlteracao(ponto, justificativa, usuario);


        return ResponseEntity.ok(PedidoAlteracaoResponse.of(pedidoAlteracao));
    }

    @DeleteMapping("/{idPedido}")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<PedidoAlteracaoResponse> apagarPedido(@PathParam("idPedido") long idPedido) {

        log.info("Apagando Pedido de Alteracao de Ponto - {}", idPedido);

        var pedidoAlteracao = pedidoAlteracaoService.apagar(idPedido);


        return ResponseEntity.ok(PedidoAlteracaoResponse.of(pedidoAlteracao));
    }

}
