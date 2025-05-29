package br.jus.trf1.sipe.registro.web;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.registro.Registro;
import br.jus.trf1.sipe.registro.RegistroService;
import br.jus.trf1.sipe.registro.web.dto.RegistroAtualizadoRequest;
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

    public RegistroUpdateController(RegistroService registroService, PontoService pontoService) {
        this.registroService = registroService;
        this.pontoService = pontoService;
    }

    @PatchMapping("/pontos")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<CollectionModel<EntityModel<RegistroResponse>>> atualizaRegistros(@RequestParam
                                                                                            String matricula,
                                                                                            @RequestParam
                                                                                            @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                            LocalDate dia) {

        log.info("Atualizando Registros do ponto - {} - {}", matricula, dia);


        Ponto ponto = pontoService.buscaPonto(matricula, dia);
        List<Registro> registros = registroService.atualizaRegistrosNovos(ponto);

        return ResponseEntity.ok(addLinksHATEOAS(registros));
    }

    @PutMapping("/pontos")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<EntityModel<RegistroResponse>> atualizaRegistro(@RequestParam
                                                                          String matricula,
                                                                          @RequestParam
                                                                          @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                          LocalDate dia,
                                                                          @RequestBody
                                                                          @Valid
                                                                          RegistroAtualizadoRequest registroAtualizadoRequest) {

        log.info("Adiciona novo registro no Ponto - {} - {}",
                matricula, paraString(dia, PADRAO_SAIDA_DATA));

        var ponto = pontoService.buscaPonto(matricula, dia);
        var registro = registroService.atualizaRegistro(ponto, registroAtualizadoRequest.toModel());
        var registroModel = EntityModel.of(RegistroResponse.of(registro),
                linkTo(methodOn(RegistroReadController.class).buscaRegistro(registro.getId())).withSelfRel());

        return ResponseEntity.ok(registroModel);
    }
}
