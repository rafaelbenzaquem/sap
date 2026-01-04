package br.jus.trf1.sipe.registro.application.web;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.service.PedidoAlteracaoServiceAdapter;
import br.jus.trf1.sipe.ponto.domain.service.PontoServiceAdapter;
import br.jus.trf1.sipe.registro.domain.model.Registro;
import br.jus.trf1.sipe.registro.application.web.dto.RegistroNovoRequest;
import br.jus.trf1.sipe.registro.application.web.dto.RegistroResponse;
import br.jus.trf1.sipe.registro.domain.port.in.RegistroServicePort;
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

    private final RegistroServicePort registroServicePort;
    private final PontoServiceAdapter pontoServiceAdapter;
    private final PedidoAlteracaoServiceAdapter pedidoAlteracaoService;


    public RegistroCreateController(RegistroServicePort registroServicePort, PontoServiceAdapter pontoServiceAdapter, PedidoAlteracaoServiceAdapter pedidoAlteracaoService) {
        this.registroServicePort = registroServicePort;
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
        List<Registro> registros = registroServicePort.salva(pedidoAlteracao,ponto,
                registrosNovos.stream().map(RegistroNovoRequest::toDomain).toList());

        return ResponseEntity.ok(addLinksHATEOAS(registros));
    }

}
