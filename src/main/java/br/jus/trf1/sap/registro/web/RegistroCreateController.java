package br.jus.trf1.sap.registro.web;

import br.jus.trf1.sap.registro.Registro;
import br.jus.trf1.sap.registro.RegistroService;
import br.jus.trf1.sap.registro.web.dto.RegistroNovoRequest;
import br.jus.trf1.sap.registro.web.dto.RegistroResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static br.jus.trf1.sap.comum.util.ConstantesParaDataTempo.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sap.comum.util.ConstantesParaDataTempo.PADRAO_SAIDA_DATA;
import static br.jus.trf1.sap.comum.util.DataTempoUtil.paraString;
import static br.jus.trf1.sap.comum.util.HATEOASUtil.addLinksHATEOAS;

@Slf4j
@RestController
@RequestMapping(value = "/v1/sap/registros")
public class RegistroCreateController {

    private final RegistroService registroService;

    public RegistroCreateController(RegistroService registroService) {
        this.registroService = registroService;
    }


    @PostMapping("/pontos")
    public ResponseEntity<CollectionModel<EntityModel<RegistroResponse>>> adicionaNovosRegistros(@RequestParam
                                                                                                 String matricula,
                                                                                                 @RequestParam
                                                                                                 @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                 LocalDate dia,
                                                                                                 @RequestBody
                                                                                                 @Valid
                                                                                                 List<RegistroNovoRequest> registrosNovos) {

        log.info("Adiciona novos registros no Ponto - {} - {} - registros size: {}",
                matricula, paraString(dia, PADRAO_SAIDA_DATA), registrosNovos.size());

        List<Registro> registros = registroService.adicionaNovosRegistros(matricula, dia,
                registrosNovos.stream().map(RegistroNovoRequest::toModel).toList());


        return ResponseEntity.ok(addLinksHATEOAS(registros));
    }

}
