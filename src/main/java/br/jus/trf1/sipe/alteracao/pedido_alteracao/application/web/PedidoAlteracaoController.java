package br.jus.trf1.sipe.alteracao.pedido_alteracao.application.web;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.port.in.PedidoAlteracaoServicePort;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.StatusPedido;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.exceptions.PedidoAlteracaoInexistenteException;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.application.web.dto.PedidoAlteracaoRequest;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.application.web.dto.PedidoAlteracaoResponse;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.application.web.dto.PedidoAlteracaoUpdateRequest;
import br.jus.trf1.sipe.comum.util.DataTempoUtil;
import br.jus.trf1.sipe.ponto.domain.port.in.PontoServicePort;
import br.jus.trf1.sipe.usuario.domain.port.out.UsuarioSecurityPort;
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

    private final PedidoAlteracaoServicePort pedidoAlteracaoServicePort;
    private final UsuarioSecurityPort usuarioSecurityPort;
    private final PontoServicePort pontoServicePort;

    public PedidoAlteracaoController(PedidoAlteracaoServicePort pedidoAlteracaoServicePort,
                                     UsuarioSecurityPort usuarioSecurityPort,
                                     PontoServicePort pontoServicePort) {
        this.pedidoAlteracaoServicePort = pedidoAlteracaoServicePort;
        this.usuarioSecurityPort = usuarioSecurityPort;
        this.pontoServicePort = pontoServicePort;
    }


    @PatchMapping
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<PedidoAlteracaoResponse> atualizaPedido(@Valid @RequestBody PedidoAlteracaoUpdateRequest pedidoAlteracaoRequest) {

        var matriculaPonto = pedidoAlteracaoRequest.matriculaPonto();
        var diaPonto = pedidoAlteracaoRequest.diaPonto();
        var justificativa = pedidoAlteracaoRequest.justificativa();
        var justificativaAprovador = pedidoAlteracaoRequest.justificativaAprovador();
        var status = StatusPedido.valueOf(pedidoAlteracaoRequest.status());
        var pedidoAlteracaoOpt = pedidoAlteracaoServicePort.buscaPedidoAlteracao(matriculaPonto, diaPonto);
        log.info("Atualizando realizando Pedido de Alteracao de Ponto - {} - {}", matriculaPonto, diaPonto);

        if (pedidoAlteracaoOpt.isPresent()) {
            var pedidoAlteracao = pedidoAlteracaoOpt.get();
            pedidoAlteracao.setJustificativa(justificativa);
            pedidoAlteracao.setJustificativaAprovador(justificativaAprovador);
            pedidoAlteracao.setStatus(status);

            pedidoAlteracaoServicePort.atualizaPedidoAlteracao(pedidoAlteracao);

            return ResponseEntity.ok(PedidoAlteracaoResponse.from(pedidoAlteracao));
        }
        throw new PedidoAlteracaoInexistenteException("Pedido de Alteracao inexistente");
    }

    @PostMapping
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<PedidoAlteracaoResponse> realizarPedido(@Valid @RequestBody PedidoAlteracaoRequest pedidoAlteracaoRequest) {

        var matriculaPonto = pedidoAlteracaoRequest.matriculaPonto();
        var diaPonto = pedidoAlteracaoRequest.diaPonto();
        var justificativa = pedidoAlteracaoRequest.justificativa();

        log.info("Atualizando realizando Pedido de Alteracao de Ponto - {} - {}", matriculaPonto, diaPonto);

        var usuario = usuarioSecurityPort.getUsuarioAutenticado();
        var ponto = pontoServicePort.buscaPonto(matriculaPonto, diaPonto);
        var pedidoAlteracao = pedidoAlteracaoServicePort.criarPedidoAlteracao(ponto, justificativa, usuario);

        return ResponseEntity.ok(PedidoAlteracaoResponse.from(pedidoAlteracao));
    }

    @DeleteMapping("/{idPedido}")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<PedidoAlteracaoResponse> apagarPedido(@PathVariable("idPedido") long idPedido) {

        log.info("Apagando Pedido de Alteracao de Ponto - {}", idPedido);

        var pedidoAlteracao = pedidoAlteracaoServicePort.apagar(idPedido);


        return ResponseEntity.ok(PedidoAlteracaoResponse.from(pedidoAlteracao));
    }


    @GetMapping("/{idPedido}")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<PedidoAlteracaoResponse> recuperarPedido(@PathVariable("idPedido") long idPedido) {

        log.info("Buscando Pedido de Alteracao de Ponto - {}", idPedido);

        var pedidoAlteracao = pedidoAlteracaoServicePort.buscaPedidoAlteracao(idPedido);


        return ResponseEntity.ok(PedidoAlteracaoResponse.from(pedidoAlteracao));
    }


    @GetMapping("/{matricula}/{dia}/pontos")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<PedidoAlteracaoResponse> recuperarPedido(@PathVariable("matricula") String matricula,
                                                                   @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                   @PathVariable("dia") LocalDate dia) {

        log.info("Buscando Pedido de Alteracao por Ponto - {} - {}", matricula, DataTempoUtil.paraString(dia));

        var pedidoAlteracao = pedidoAlteracaoServicePort.buscaPedidoAlteracao(matricula, dia).orElseThrow(() ->
                new PedidoAlteracaoInexistenteException("Não existe pedido de alteração para o ponto matricula: " + matricula + " dia: " + DataTempoUtil.paraString(dia)));
        ;

        return ResponseEntity.ok(PedidoAlteracaoResponse.from(pedidoAlteracao));
    }

}
