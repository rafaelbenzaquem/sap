package br.jus.trf1.sipe.alteracao.pedido_alteracao.web;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracaoService;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.web.dto.PedidoAlteracaoRequest;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.web.dto.PedidoAlteracaoResponse;
import br.jus.trf1.sipe.comum.util.DataTempoUtil;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.usuario.UsuarioAtualService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;

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


        return ResponseEntity.ok(PedidoAlteracaoResponse.from(pedidoAlteracao));
    }

    @DeleteMapping("/{idPedido}")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<PedidoAlteracaoResponse> apagarPedido(@PathVariable("idPedido") long idPedido) {

        log.info("Apagando Pedido de Alteracao de Ponto - {}", idPedido);

        var pedidoAlteracao = pedidoAlteracaoService.apagar(idPedido);


        return ResponseEntity.ok(PedidoAlteracaoResponse.from(pedidoAlteracao));
    }


    @GetMapping("/{idPedido}")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<PedidoAlteracaoResponse> recuperarPedido(@PathVariable("idPedido") long idPedido) {

        log.info("Buscando Pedido de Alteracao de Ponto - {}", idPedido);

        var pedidoAlteracao = pedidoAlteracaoService.buscaPedidoAlteracao(idPedido);


        return ResponseEntity.ok(PedidoAlteracaoResponse.from(pedidoAlteracao));
    }


    @GetMapping("/{matricula}/{dia}/pontos")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<PedidoAlteracaoResponse> recuperarPedido(@PathVariable("matricula") String matricula,
                                                                   @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                   @PathVariable("dia") LocalDate dia) {

        log.info("Buscando Pedido de Alteracao por Ponto - {} - {}", matricula, DataTempoUtil.paraString(dia));

        var pedidoAlteracao = pedidoAlteracaoService.buscaPedidoAlteracao(matricula, dia);

        return ResponseEntity.ok(PedidoAlteracaoResponse.from(pedidoAlteracao));
    }

    @GetMapping("/{idPedido}")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<PedidoAlteracaoResponse> buscarPedido(@PathVariable("idPedido") long idPedido) {

        log.info("Apagando Pedido de Alteracao de Ponto - {}", idPedido);

        var pedidoAlteracao = pedidoAlteracaoService.buscaPedidoAlteracao(idPedido);


        return ResponseEntity.ok(PedidoAlteracaoResponse.of(pedidoAlteracao));
    }

}
