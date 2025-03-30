package br.jus.trf1.sap.ponto.web;

import br.jus.trf1.sap.ponto.PontoService;
import br.jus.trf1.sap.ponto.web.dto.PontoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

import static br.jus.trf1.sap.comum.util.ConstantesDataTempoUtil.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sap.comum.util.DataTempoUtil.paraString;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/v1/sap/pontos")
public class BuscaPontosController {

    private final PontoService pontoService;


    public BuscaPontosController(PontoService pontoService) {
        this.pontoService = pontoService;
    }

    @GetMapping("/{matricula}/{dia}")
    public ResponseEntity<EntityModel<PontoResponse>> buscaPonto(@PathVariable
                                                                 String matricula,
                                                                 @PathVariable
                                                                 @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                 LocalDate dia) {
        var diaFormatado = paraString(dia);
        log.info("Buscando Ponto - {} - {}", matricula, diaFormatado);
        var ponto = pontoService.buscaPonto(matricula, dia);
        var uri = ServletUriComponentsBuilder.fromCurrentContextPath().
                path("/v1/sap/pontos/{matricula}/{dia}/registros").
                buildAndExpand(matricula, diaFormatado).toUriString();
        var pontoModel = EntityModel.of(PontoResponse.of(ponto),
                linkTo(methodOn(BuscaPontosController.class).buscaPonto(matricula, dia)).withSelfRel(),
                Link.of(uri).withRel("registros")
        );
        return ResponseEntity.ok(pontoModel);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PontoResponse>>> buscaPontosPorIntervalosDatas(@RequestParam
                                                                                                     String matricula,
                                                                                                     @RequestParam
                                                                                                     @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                     LocalDate inicio,
                                                                                                     @RequestParam
                                                                                                     @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                     LocalDate fim) {

        var pontos = pontoService.buscarPontos(matricula, inicio, fim);

        var pontosEntityModelList = pontos.stream().map(ponto -> {
            var dia = ponto.getId().getDia();
            var diaFormatado = paraString(dia);
            var uri = ServletUriComponentsBuilder.fromCurrentContextPath().
                    path("/v1/sap/pontos/{matricula}/{dia}/registros").
                    buildAndExpand(matricula, diaFormatado).toUriString();
            return EntityModel.of(PontoResponse.of(ponto),
                    linkTo(methodOn(BuscaPontosController.class).buscaPonto(matricula, dia)).withSelfRel(),
                    Link.of(uri).withRel("registros")
            );
        }).toList();

        var pontosCollectionModel = CollectionModel.of(pontosEntityModelList,
                linkTo(methodOn(BuscaPontosController.class).
                        buscaPontosPorIntervalosDatas(matricula, inicio, fim)).
                        withSelfRel()
        );

        return ResponseEntity.ok(pontosCollectionModel);
    }

}
