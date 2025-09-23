package br.jus.trf1.sipe.ponto.web;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.ponto.web.dto.PontoResponse;
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

    private final PontoService pontoService;


    public PontoReadController(PontoService pontoService) {
        this.pontoService = pontoService;
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
        var ponto = pontoService.buscaPonto(matricula, dia);
        var uri = ServletUriComponentsBuilder.fromCurrentContextPath().
                path("/v1/sipe/registros/pontos?matricula={matricula}&dia={dia}").
                buildAndExpand(matricula, diaFormatado).toUriString();
        var pontoModel = EntityModel.of(PontoResponse.of(ponto),
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
                                                                                                     Boolean pendente) {

        var pontos = pendente ? pontoService.buscarPontos(matricula, inicio, fim).stream().
                filter(Ponto::pedidoAlteracaoPendente).toList() : pontoService.buscarPontos(matricula, inicio, fim);

        if (pontos.isEmpty()) {
            return ResponseEntity.ok(CollectionModel.empty());
        }

        var pontosEntityModelList = pontos.stream().map(ponto -> {
            var dia = ponto.getId().getDia();
            var diaFormatado = paraString(dia);
            var uri = ServletUriComponentsBuilder.fromCurrentContextPath().
                    path("/v1/sipe/registros/pontos?matricula={matricula}&dia={dia}").
                    buildAndExpand(matricula, diaFormatado).toUriString();
            return EntityModel.of(PontoResponse.of(ponto),
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

        Boolean temPontoPendente = pontoService.existePontoComPedidoAlteracaoPendenteNoPeriodo(matricula,inicio,fim);

        return ResponseEntity.ok(temPontoPendente);
    }

}
