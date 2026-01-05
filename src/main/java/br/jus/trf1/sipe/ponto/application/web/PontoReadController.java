package br.jus.trf1.sipe.ponto.application.web;

import br.jus.trf1.sipe.ponto.application.web.dto.PontoResponse;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.ponto.domain.port.in.PontoServicePort;
import br.jus.trf1.sipe.ponto.exceptions.PontoInexistenteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.DataTempoUtil.paraString;
import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/v1/sipe/pontos")
public class PontoReadController {

    private final PontoServicePort pontoServicePort;


    public PontoReadController(PontoServicePort pontoServicePort) {
        this.pontoServicePort = pontoServicePort;
    }

    @GetMapping("/{matricula}/{dia}")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<EntityModel<PontoResponse>> buscaPonto(@PathVariable
                                                                 String matricula,
                                                                 @PathVariable
                                                                 @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                 LocalDate dia) {
        var diaFormatado = paraString(dia);
        log.info("Buscando Ponto - {} - {}", matricula, diaFormatado);
        var pontoOpt = pontoServicePort.buscaPonto(matricula, dia);
        var ponto = pontoOpt.orElseThrow(() -> new PontoInexistenteException(matricula, dia));
        var uri = ServletUriComponentsBuilder.fromCurrentContextPath().
                path("/v1/sipe/registros/pontos?matricula={matricula}&dia={dia}").
                buildAndExpand(matricula, diaFormatado).toUriString();
        var pontoModel = EntityModel.of(PontoWebMapper.toResponse(ponto),
                linkTo(methodOn(PontoReadController.class).buscaPonto(matricula, dia)).withSelfRel(),
                Link.of(uri).withRel("registros")
        );
        return ResponseEntity.ok(pontoModel);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<CollectionModel<EntityModel<PontoResponse>>> buscaPontosPorIntervalosDatas(@RequestParam
                                                                                                     String matricula,
                                                                                                     @RequestParam
                                                                                                     @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                     LocalDate inicio,
                                                                                                     @RequestParam
                                                                                                     @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                     LocalDate fim,
                                                                                                     @RequestParam(defaultValue = "false")
                                                                                                     boolean pendente) {

        var pontos = pendente ? pontoServicePort.buscarPontos(matricula, inicio, fim).stream().
                filter(Ponto::pedidoAlteracaoPendente).toList() : pontoServicePort.buscarPontos(matricula, inicio, fim);

        if (pontos.isEmpty()) {
            return ResponseEntity.ok(CollectionModel.empty());
        }

        var pontosEntityModelList = pontos.stream().map(ponto -> {
            var dia = ponto.getId().getDia();
            var diaFormatado = paraString(dia);
            var uri = ServletUriComponentsBuilder.fromCurrentContextPath().
                    path("/v1/sipe/registros/pontos?matricula={matricula}&dia={dia}").
                    buildAndExpand(matricula, diaFormatado).toUriString();
            return EntityModel.of(PontoWebMapper.toResponse(ponto),
                    linkTo(methodOn(PontoReadController.class).buscaPonto(matricula, dia)).withSelfRel(),
                    Link.of(uri).withRel("registros")
            );
        }).toList();

        var pontosCollectionModel = CollectionModel.of(pontosEntityModelList,
                linkTo(methodOn(PontoReadController.class).
                        buscaPontosPorIntervalosDatas(matricula, inicio, fim, pendente)).
                        withSelfRel()
        );

        return ResponseEntity.ok(pontosCollectionModel);
    }


    @GetMapping("/pendente")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<Boolean> existePontoPendenteNoPeriodo(@RequestParam
                                                                String matricula,
                                                                @RequestParam
                                                                @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                LocalDate inicio,
                                                                @RequestParam
                                                                @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                LocalDate fim){

        Boolean temPontoPendente = pontoServicePort.existePontoComPedidoAlteracaoPendenteNoPeriodo(matricula,inicio,fim);

        return ResponseEntity.ok(temPontoPendente);
    }

}
