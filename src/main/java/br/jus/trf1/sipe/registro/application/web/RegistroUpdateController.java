package br.jus.trf1.sipe.registro.application.web;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.port.in.PedidoAlteracaoServicePort;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.ponto.domain.port.in.PontoServicePort;
import br.jus.trf1.sipe.ponto.exceptions.PontoInexistenteException;
import br.jus.trf1.sipe.registro.domain.model.Registro;
import br.jus.trf1.sipe.registro.application.web.dto.RegistroAtualizadoRequest;
import br.jus.trf1.sipe.registro.application.web.dto.RegistroResponse;
import br.jus.trf1.sipe.registro.domain.port.in.RegistroServicePort;
import br.jus.trf1.sipe.servidor.domain.port.in.ServidorServicePort;
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

    private final RegistroServicePort registroServicePort;
    private final PontoServicePort pontoServicePort;
    private final ServidorServicePort servidorServicePort;
    private final PedidoAlteracaoServicePort pedidoAlteracaoServicePort;


    public RegistroUpdateController(RegistroServicePort registroServicePort,
                                    PontoServicePort pontoServicePort,
                                    ServidorServicePort servidorServicePort,
                                    PedidoAlteracaoServicePort pedidoAlteracaoServicePort) {
        this.registroServicePort = registroServicePort;
        this.pontoServicePort = pontoServicePort;
        this.servidorServicePort = servidorServicePort;
        this.pedidoAlteracaoServicePort = pedidoAlteracaoServicePort;
    }

    @PatchMapping("/pontos")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<CollectionModel<EntityModel<RegistroResponse>>> atualizaRegistros(@RequestParam
                                                                                            String matricula,
                                                                                            @RequestParam
                                                                                            @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                            LocalDate dia) {

        log.info("Atualizando Registros do ponto - {} - {}", matricula, dia);


        Ponto ponto = pontoServicePort.buscaPonto(matricula, dia).
                orElseThrow(() -> new PontoInexistenteException(matricula, dia));
        List<Registro> registros = registroServicePort.salvaNovosDeSistemaExternoEmBaseInterna(ponto);

        return ResponseEntity.ok(addLinksHATEOAS(registros));
    }

    @PutMapping("/{id_registro}")
    @PreAuthorize("hasAnyAuthority('GRP_SIPE_ADMIN', 'GRP_SIPE_RH', 'GRP_SIPE_DIRETOR')")
    public ResponseEntity<EntityModel<RegistroResponse>> aprovaRegistro(@PathVariable("id_registro") Long idRegistro) {

        var registro = registroServicePort.aprova(idRegistro);

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
        var pedidoAlteracao = pedidoAlteracaoServicePort.buscaPedidoAlteracao(idPedidoAlteracao);
        var ponto = pontoServicePort.buscaPonto(matricula, dia).orElseThrow(() -> new PontoInexistenteException(matricula, dia));
        var registroAtualizado = registroServicePort.atualiza(pedidoAlteracao, ponto, registroAtualizadoRequest.toModel());
        var registroModel = EntityModel.of(RegistroResponse.of(registroAtualizado),
                linkTo(methodOn(RegistroReadController.class).buscaRegistro(registroAtualizado.getId())).withSelfRel());

        return ResponseEntity.ok(registroModel);
    }
}
