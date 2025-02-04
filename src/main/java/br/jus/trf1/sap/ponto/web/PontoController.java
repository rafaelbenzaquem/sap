package br.jus.trf1.sap.ponto.web;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoService;
import br.jus.trf1.sap.ponto.web.dto.PontoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static br.jus.trf1.sap.util.DateTimeUtils.*;

@Slf4j
@RestController
@RequestMapping("/v1/sap/pontos")
public class PontoController {

    private final PontoService pontoService;

    public PontoController(PontoService pontoService) {
        this.pontoService = pontoService;
    }

    @GetMapping("/{matricula}/{dia}")
    public ResponseEntity<PontoResponse> buscarPonto(@PathVariable Integer matricula, @PathVariable String dia) {

        log.info("Buscando Ponto - {} - {}", matricula, dia);
        Optional<Ponto> pontoOpt = pontoService.buscarPonto(matricula, criaLocalDate(dia));
        return pontoOpt.map(ponto -> ResponseEntity.ok(PontoResponse.of(ponto))).
                orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{matricula}/{dia}")
    public ResponseEntity<PontoResponse> buscarAtualizacaoPonto(@PathVariable Integer matricula, @PathVariable String dia) {

        log.info("Atualizando Ponto - {} - {}", matricula, dia);
        Optional<Ponto> pontoOpt = pontoService.buscarAtualizarRegistrosPonto(matricula, criaLocalDate(dia));
        return pontoOpt.map(ponto -> ResponseEntity.ok(PontoResponse.of(ponto))).
                orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<List<PontoResponse>> buscarPontosPorIntervalosDatas(@PathVariable Integer matricula,
                                                                              @RequestParam String inicio,
                                                                              @RequestParam String fim) {

        var pontos = pontoService.buscarPontos(matricula, criaLocalDate(inicio), criaLocalDate(fim));
        return ResponseEntity.ok(pontos.stream().map(PontoResponse::of).toList());
    }

}
