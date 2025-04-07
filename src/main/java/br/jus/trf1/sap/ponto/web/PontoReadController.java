package br.jus.trf1.sap.ponto.web;

import br.jus.trf1.sap.ponto.PontoService;
import br.jus.trf1.sap.ponto.web.dto.PontoNovoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;

import static br.jus.trf1.sap.comum.util.ConstantesParaDataTempo.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sap.comum.util.DataTempoUtil.paraString;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/v1/sap/pontos")
public class PontoReadController {

    private final PontoService pontoService;


    public PontoReadController(PontoService pontoService) {
        this.pontoService = pontoService;
    }

    @GetMapping("/{matricula}/{dia}")
    public ResponseEntity<EntityModel<PontoNovoResponse>> buscaPonto(@PathVariable
                                                                     String matricula,
                                                                     @PathVariable
                                                                     @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                     LocalDate dia) {
        var diaFormatado = paraString(dia);
        log.info("Buscando Ponto - {} - {}", matricula, diaFormatado);
        var ponto = pontoService.buscaPonto(matricula, dia);
        var uri = ServletUriComponentsBuilder.fromCurrentContextPath().
                path("/v1/sap/registros/pontos?matricula={matricula}&dia={dia}").
                buildAndExpand(matricula, diaFormatado).toUriString();
        var pontoModel = EntityModel.of(PontoNovoResponse.of(ponto),
                linkTo(methodOn(PontoReadController.class).buscaPonto(matricula, dia)).withSelfRel(),
                Link.of(uri).withRel("registros")
        );
        return ResponseEntity.ok(pontoModel);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PontoNovoResponse>>> buscaPontosPorIntervalosDatas(@RequestParam
                                                                                                         String matricula,
                                                                                                         @RequestParam
                                                                                                         @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                         LocalDate inicio,
                                                                                                         @RequestParam
                                                                                                         @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                         LocalDate fim) {

        var pontos = pontoService.buscarPontos(matricula, inicio, fim);

        if (pontos.isEmpty()) {
            return ResponseEntity.ok(CollectionModel.empty());
        }

        var pontosEntityModelList = pontos.stream().map(ponto -> {
            var dia = ponto.getId().getDia();
            var diaFormatado = paraString(dia);
            var uri = ServletUriComponentsBuilder.fromCurrentContextPath().
                    path("/v1/sap/registros/pontos?matricula={matricula}&dia={dia}").
                    buildAndExpand(matricula, diaFormatado).toUriString();
            return EntityModel.of(PontoNovoResponse.of(ponto),
                    linkTo(methodOn(PontoReadController.class).buscaPonto(matricula, dia)).withSelfRel(),
                    Link.of(uri).withRel("registros")
            );
        }).toList();

        var pontosCollectionModel = CollectionModel.of(pontosEntityModelList,
                linkTo(methodOn(PontoReadController.class).
                        buscaPontosPorIntervalosDatas(matricula, inicio, fim)).
                        withSelfRel()
        );

        return ResponseEntity.ok(pontosCollectionModel);
    }

}
