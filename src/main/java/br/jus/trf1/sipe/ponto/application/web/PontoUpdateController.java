package br.jus.trf1.sipe.ponto.application.web;

import br.jus.trf1.sipe.ponto.application.web.dto.PontoAtualizadoRequest;
import br.jus.trf1.sipe.ponto.application.web.dto.PontoAtualizadoResponse;
import br.jus.trf1.sipe.ponto.application.web.dto.PontoNovoResponse;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.ponto.domain.port.in.PontoServicePort;
import br.jus.trf1.sipe.registro.domain.port.in.RegistroServicePort;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

import static br.jus.trf1.sipe.comum.util.DataTempoUtil.paraString;
import static br.jus.trf1.sipe.comum.util.HATEOASUtil.addLinksHATEOAS;
import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/v1/sipe/pontos")
public class PontoUpdateController {

    private final PontoServicePort pontoServicePort;
    private final RegistroServicePort registroServicePort;

    public PontoUpdateController(PontoServicePort pontoServicePort,RegistroServicePort registroServicePort) {
        this.pontoServicePort = pontoServicePort;
        this.registroServicePort =registroServicePort;
    }


    @PutMapping
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<EntityModel<PontoAtualizadoResponse>> atualizaPonto(@RequestBody
                                                                              @Valid
                                                                              PontoAtualizadoRequest pontoAtualizadoRequest) {
        log.info("atualizaPonto - {}", pontoAtualizadoRequest);
        var dia = pontoAtualizadoRequest.dia();
        var diaFormatado = paraString(dia, PADRAO_ENTRADA_DATA);
        var matricula = pontoAtualizadoRequest.matricula();

       var pontoAualizado = pontoServicePort.atualizaPonto(pontoAtualizadoRequest.toDomain());

        var uri = ServletUriComponentsBuilder.fromCurrentContextPath().
                path("/v1/sap/pontos?matricula={matricula}&dia={dia}").
                buildAndExpand(matricula, diaFormatado).toUri();
        var pontoModel = EntityModel.of(PontoWebMapper.toAtualizadoReponse(pontoAualizado),
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
    @Transactional
    @PostMapping("/usuarios")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
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
        List<Ponto> pontos = pontoServicePort.carregaPontos(matricula, inicio, fim);
        pontos.forEach(registroServicePort::salvaNovosDeSistemaExternoEmBaseInterna);
        return ResponseEntity.status(HttpStatus.CREATED).body(addLinksHATEOAS(inicio, fim, false, pontos));
    }

}
