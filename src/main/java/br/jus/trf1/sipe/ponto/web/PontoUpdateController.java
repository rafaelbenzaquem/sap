package br.jus.trf1.sipe.ponto.web;

import br.jus.trf1.sipe.ponto.IndicePonto;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.ponto.web.dto.PontoAtualizadoRequest;
import br.jus.trf1.sipe.ponto.web.dto.PontoAtualizadoResponse;
import br.jus.trf1.sipe.ponto.web.dto.PontoNovoResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

import static br.jus.trf1.sipe.comum.util.HATEOASUtil.addLinksHATEOAS;
import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sipe.comum.util.DataTempoUtil.paraString;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/v1/sipe/pontos")
public class PontoUpdateController {

    private final PontoService pontoService;

    public PontoUpdateController(PontoService pontoService) {
        this.pontoService = pontoService;
    }


    @PutMapping
    public ResponseEntity<EntityModel<PontoAtualizadoResponse>> atualizaPonto(@RequestBody
                                                                              @Valid
                                                                              PontoAtualizadoRequest pontoAtualizadoRequest) {
        log.info("atualizaPonto - {}", pontoAtualizadoRequest);
        var dia = pontoAtualizadoRequest.dia();
        var diaFormatado = paraString(dia, PADRAO_ENTRADA_DATA);
        var matricula = pontoAtualizadoRequest.matricula();
        var descricao = pontoAtualizadoRequest.descricao();
        var ponto = pontoService.buscaPonto(matricula, dia);

        ponto.setDescricao(descricao);

        pontoService.atualizaPonto(ponto);

        var uri = ServletUriComponentsBuilder.fromCurrentContextPath().
                path("/v1/sap/pontos?matricula={matricula}&dia={dia}").
                buildAndExpand(matricula, diaFormatado).toUri();
        var pontoModel = EntityModel.of(PontoAtualizadoResponse.of(ponto),
                linkTo(methodOn(PontoReadController.class).buscaPonto(matricula, dia)).withSelfRel()
        );

        return ResponseEntity.created(uri).body(pontoModel);
    }


    /**
     * Carrega pontos para um usuário a partir da sua matrícula e um período definido (início e fim), preenchendo os
     * pontos com os registros proveniente do histórico do sistema de controle de acesso, os pontos existentes serão
     * atualizados com os novos registros e os pontos não existentes serão criados com os registros existentes no
     * sistema de ponto
     *
     * @param matricula Matrícula do usuário que representa parte do identificador de ponto
     * @param inicio    LocaDate que representa o início do período, não pode ser nulo
     * @param fim       LocalDate que representa o fim do período, pode ser nulo
     * @return Coleção com pontos criados formatado para restfull
     */
    @PostMapping("/usuarios")
    public ResponseEntity<CollectionModel<EntityModel<PontoNovoResponse>>> carregaPontosPorPeriodo(@RequestParam
                                                                                                   String matricula,
                                                                                                   @RequestParam
                                                                                                   @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                   LocalDate inicio,
                                                                                                   @RequestParam
                                                                                                   @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                   LocalDate fim) {
        log.info("PontoUpdateController:carregaPontosPorPeriodo");
        log.info("Criando Pontos de - {} - {}", paraString(inicio), paraString(fim));
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim.");
        }
        List<Ponto> pontos = pontoService.carregaPontos(matricula, inicio, fim);
        return ResponseEntity.status(HttpStatus.CREATED).body(addLinksHATEOAS(inicio, fim, pontos));
    }

}
