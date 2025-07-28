package br.jus.trf1.sipe.alteracao.pedido_alteracao.web;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracaoService;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.web.dto.PedidoAlteracaoResponse;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.usuario.UsuarioAtualService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    @PatchMapping()
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<PedidoAlteracaoResponse> realizarPedido(@RequestParam("matricula_ponto")
                                                                  String matriculaPonto,
                                                                  @RequestParam("dia_ponto")
                                                                  @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                  LocalDate diaPonto,
                                                                  @RequestParam
                                                                  String justificativa) {

        log.info("Atualizando realizando Pedido de Alteracao de Ponto - {} - {}", matriculaPonto, diaPonto);

        var usuario = usuarioAtualService.getUsuario();
        var ponto = pontoService.buscaPonto(matriculaPonto, diaPonto);
        var pedidoAlteracao = pedidoAlteracaoService.criarPedidoAlteracao(ponto, justificativa, usuario);


        return ResponseEntity.ok(PedidoAlteracaoResponse.of(pedidoAlteracao));
    }

}
