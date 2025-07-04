package br.jus.trf1.sipe.registro.web;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.registro.Registro;
import br.jus.trf1.sipe.registro.RegistroService;
import br.jus.trf1.sipe.registro.web.dto.RegistroNovoRequest;
import br.jus.trf1.sipe.registro.web.dto.RegistroResponse;
import br.jus.trf1.sipe.servidor.ServidorService;
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

@Slf4j
@RestController
@RequestMapping(value = "/v1/sipe/registros")
public class RegistroCreateController {

    private final RegistroService registroService;
    private final PontoService pontoService;
    private final ServidorService servidorService;

    public RegistroCreateController(RegistroService registroService, PontoService pontoService, ServidorService servidorService) {
        this.registroService = registroService;
        this.pontoService = pontoService;
        this.servidorService = servidorService;
    }


    @PostMapping("/pontos")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<CollectionModel<EntityModel<RegistroResponse>>> adicionaNovosRegistros(@RequestParam("matricula_ponto")
                                                                                                 String matriculaPonto,
                                                                                                 @RequestParam("matricula_criador")
                                                                                                 String matriculaCriador,
                                                                                                 @RequestParam
                                                                                                 @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                 LocalDate dia,
                                                                                                 @RequestBody
                                                                                                 @Valid
                                                                                                 List<RegistroNovoRequest> registrosNovos) {

        log.info("Adiciona novos registros no Ponto - {} - {} - registros size: {}",
                matriculaPonto, paraString(dia, PADRAO_SAIDA_DATA), registrosNovos.size());
        var servidorCriador = servidorService.buscaPorMatricula(matriculaCriador);
        Ponto ponto = pontoService.buscaPonto(matriculaPonto, dia);
        List<Registro> registros = registroService.addRegistros(ponto,
                registrosNovos.stream().map(r-> r.toModel(servidorCriador)).toList());

        return ResponseEntity.ok(addLinksHATEOAS(registros));
    }

}
