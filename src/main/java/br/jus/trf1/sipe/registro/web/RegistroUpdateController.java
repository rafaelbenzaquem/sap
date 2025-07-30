package br.jus.trf1.sipe.registro.web;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracaoService;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.registro.Registro;
import br.jus.trf1.sipe.registro.RegistroService;
import br.jus.trf1.sipe.registro.web.dto.RegistroAtualizadoRequest;
import br.jus.trf1.sipe.registro.web.dto.RegistroResponse;
import br.jus.trf1.sipe.servidor.ServidorService;
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

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_SAIDA_DATA;
import static br.jus.trf1.sipe.comum.util.DataTempoUtil.paraString;
import static br.jus.trf1.sipe.comum.util.HATEOASUtil.addLinksHATEOAS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/v1/sipe/registros")
public class RegistroUpdateController {

    private final RegistroService registroService;
    private final PontoService pontoService;
    private final ServidorService servidorService;
    private final PedidoAlteracaoService pedidoAlteracaoService;


    public RegistroUpdateController(RegistroService registroService, PontoService pontoService, ServidorService servidorService, PedidoAlteracaoService pedidoAlteracaoService) {
        this.registroService = registroService;
        this.pontoService = pontoService;
        this.servidorService = servidorService;
        this.pedidoAlteracaoService = pedidoAlteracaoService;
    }

    @PatchMapping("/pontos")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<CollectionModel<EntityModel<RegistroResponse>>> atualizaRegistros(@RequestParam
                                                                                            String matriculaPonto,
                                                                                            @RequestParam
                                                                                            @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                            LocalDate dia) {

        log.info("Atualizando Registros do ponto - {} - {}", matriculaPonto, dia);


        Ponto ponto = pontoService.buscaPonto(matriculaPonto, dia);
        List<Registro> registros = registroService.atualizaRegistrosSistemaDeAcesso(ponto);

        return ResponseEntity.ok(addLinksHATEOAS(registros));
    }

    @PutMapping("/{id_registro}")
    @PreAuthorize("hasAnyAuthority('GRP_SIPE_ADMIN', 'GRP_SIPE_RH', 'GRP_SIPE_DIRETOR')")
    public ResponseEntity<EntityModel<RegistroResponse>> aprovaRegistro(@PathVariable("id_registro") Long idRegistro) {

        var registro = registroService.aprovarRegistro(idRegistro);

        var registroModel = EntityModel.of(RegistroResponse.of(registro),
                linkTo(methodOn(RegistroReadController.class).buscaRegistro(registro.getId())).withSelfRel());

        return ResponseEntity.ok(registroModel);
    }


    @PutMapping("/pontos")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<EntityModel<RegistroResponse>> atualizaRegistro(@RequestParam("matricula")
                                                                          String matricula,
                                                                          @RequestParam
                                                                          @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                          LocalDate dia,
                                                                          @RequestParam("id_pedido_alteracao")
                                                                          Long idPedidoAlteracao,
                                                                          @RequestBody
                                                                          @Valid
                                                                          RegistroAtualizadoRequest registroAtualizadoRequest) {

        log.info("Adiciona novo registro no Ponto - {} - {}",
                matricula, paraString(dia, PADRAO_SAIDA_DATA));
        var pedidoAlteracao = pedidoAlteracaoService.buscaPedidoAlteracao(idPedidoAlteracao);
        var ponto = pontoService.buscaPonto(matricula, dia);
        var registroAtualizado = registroService.atualizaRegistro(pedidoAlteracao, ponto, registroAtualizadoRequest.toModel());
        var registroModel = EntityModel.of(RegistroResponse.of(registroAtualizado),
                linkTo(methodOn(RegistroReadController.class).buscaRegistro(registroAtualizado.getId())).withSelfRel());

        return ResponseEntity.ok(registroModel);
    }
}
