package br.jus.trf1.sap.ponto.web;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoService;
import br.jus.trf1.sap.ponto.web.dto.PontoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static br.jus.trf1.sap.util.ConstantesDataTempoUtil.PADRAO_ENTRADA_DATA;

@Slf4j
@RestController
@RequestMapping("/v1/sap/pontos")
public class BuscaPontosController {

    private final PontoService pontoService;


    public BuscaPontosController(PontoService pontoService) {
        this.pontoService = pontoService;
    }

    @GetMapping("/{matricula}/{dia}")
    public ResponseEntity<PontoResponse> buscaPonto(@PathVariable String matricula,
                                                    @PathVariable
                                                    @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                    LocalDate dia) {
        log.info("Buscando Ponto - {} - {}", matricula, dia);
        Optional<Ponto> pontoOpt = pontoService.buscaPonto(matricula, dia);
        return pontoOpt.map(ponto -> ResponseEntity.ok(PontoResponse.of(ponto))).
                orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<List<PontoResponse>> buscaPontosPorIntervalosDatas(@PathVariable String matricula,
                                                                             @RequestParam
                                                                             @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                             LocalDate inicio,
                                                                             @RequestParam
                                                                             @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                             LocalDate fim) {

        var pontos = pontoService.buscarPontos(matricula, inicio, fim);
        return ResponseEntity.ok(pontos.stream().map(PontoResponse::of).toList());
    }

}
