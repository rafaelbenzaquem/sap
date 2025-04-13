package br.jus.trf1.sipe.registro.web;

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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static br.jus.trf1.sipe.comum.util.ConstantesParaDataTempo.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sipe.comum.util.ConstantesParaDataTempo.PADRAO_SAIDA_DATA;
import static br.jus.trf1.sipe.comum.util.DataTempoUtil.paraString;
import static br.jus.trf1.sipe.comum.util.HATEOASUtil.addLinksHATEOAS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/v1/sipe/registros")
public class RegistroUpdateController {

    private final RegistroService registroService;

    public RegistroUpdateController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @PatchMapping("/pontos")
    public ResponseEntity<CollectionModel<EntityModel<RegistroResponse>>> atualizaRegistros(@RequestParam
                                                                                            String matricula,
                                                                                            @RequestParam
                                                                                            @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                            LocalDate dia) {

        log.info("Atualizando Registros do ponto - {} - {}", matricula, dia);

        List<Registro> registros = registroService.atualizaRegistrosNovos(matricula, dia);

        return ResponseEntity.ok(addLinksHATEOAS(registros));
    }

    @PutMapping("/pontos")
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



        Registro registro = registroService.atualizaRegistro(matricula, dia,registroAtualizadoRequest.toModel());
        var registroModel = EntityModel.of(RegistroResponse.of(registro),
                linkTo(methodOn(RegistroReadController.class).buscaRegistro(registro.getId())).withSelfRel());

        return ResponseEntity.ok(registroModel);
    }
}
