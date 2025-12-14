package br.jus.trf1.sipe.registro.web;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracaoService;
import br.jus.trf1.sipe.ponto.domain.service.PontoServiceAdapter;
import br.jus.trf1.sipe.registro.Registro;
import br.jus.trf1.sipe.registro.RegistroService;
import br.jus.trf1.sipe.registro.web.dto.RegistroNovoRequest;
import br.jus.trf1.sipe.registro.web.dto.RegistroResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static br.jus.trf1.sipe.comum.util.DataTempoUtil.paraString;
import static br.jus.trf1.sipe.comum.util.HATEOASUtil.addLinksHATEOAS;
import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_SAIDA_DATA;

@Slf4j
@RestController
@RequestMapping(value = "/v1/sipe/registros")
public class RegistroCreateController {

    private final RegistroService registroService;
    private final PontoServiceAdapter pontoServiceAdapter;
    private final PedidoAlteracaoService pedidoAlteracaoService;


    public RegistroCreateController(RegistroService registroService, PontoServiceAdapter pontoServiceAdapter, PedidoAlteracaoService pedidoAlteracaoService) {
        this.registroService = registroService;
        this.pontoServiceAdapter = pontoServiceAdapter;
        this.pedidoAlteracaoService = pedidoAlteracaoService;
    }

    @PostMapping("/pontos")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<CollectionModel<EntityModel<RegistroResponse>>> adicionaNovosRegistros(@RequestParam("matricula")
                                                                                                 String matricula,
                                                                                                 @RequestParam("dia")
                                                                                                 @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                 LocalDate dia,
                                                                                                 @RequestParam("id_pedido_alteracao")
                                                                                                 Long idPedidoAlteracao,
                                                                                                 @RequestBody
                                                                                                 @Valid
                                                                                                 List<RegistroNovoRequest> registrosNovos) {

        log.info("Adiciona novos registros no Ponto - {} - {} - registros size: {}",
                matricula, paraString(dia, PADRAO_SAIDA_DATA), registrosNovos.size());

        var pedidoAlteracao = pedidoAlteracaoService.buscaPedidoAlteracao(idPedidoAlteracao);
        var ponto = pontoServiceAdapter.buscaPonto(matricula, dia);
        List<Registro> registros = registroService.addRegistros(pedidoAlteracao,ponto,
                registrosNovos.stream().map(RegistroNovoRequest::toModel).toList());

        return ResponseEntity.ok(addLinksHATEOAS(registros));
    }

}
