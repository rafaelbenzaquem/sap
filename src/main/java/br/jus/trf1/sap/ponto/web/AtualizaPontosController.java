package br.jus.trf1.sap.ponto.web;

import br.jus.trf1.sap.externo.coletor.historico.HistoricoService;
import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoService;
import br.jus.trf1.sap.ponto.web.dto.PontoResponse;
import br.jus.trf1.sap.registro.web.RegistroNovoRequest;
import br.jus.trf1.sap.vinculo.Vinculo;
import br.jus.trf1.sap.vinculo.VinculoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static br.jus.trf1.sap.util.ConstantesDataTempoUtil.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sap.util.DataTempoUtil.paraStringData;

@Slf4j
@RestController
@RequestMapping("/v1/sap/pontos")
public class AtualizaPontosController {

    private final PontoService pontoService;
    private final VinculoService vinculoService;
    private final HistoricoService historicoService;

    public AtualizaPontosController(PontoService pontoService, VinculoService vinculoService, HistoricoService historicoService) {
        this.pontoService = pontoService;
        this.vinculoService = vinculoService;
        this.historicoService = historicoService;
    }

    @PatchMapping("/{matricula}/{dia}")
    public ResponseEntity<PontoResponse> atualizaPonto(@PathVariable
                                                       String matricula,
                                                       @PathVariable
                                                       @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                       LocalDate dia) {

        log.info("Atualizando Ponto - {} - {}", matricula, dia);
        Vinculo vinculo = vinculoService.buscaPorMatricula(matricula);
        var historicos = historicoService.buscarHistoricoDeAcesso(
                dia, null, vinculo.getCracha(), null, null);
        Optional<Ponto> pontoOpt = pontoService.atualizaRegistrosPonto(matricula, dia, historicos);
        return pontoOpt.map(ponto -> ResponseEntity.ok(PontoResponse.of(ponto))).
                orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{matricula}/{dia}/registros")
    public ResponseEntity<PontoResponse> adicionaNovosRegistrosPonto(@PathVariable
                                                                     String matricula,
                                                                     @PathVariable
                                                                     @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                     LocalDate dia,
                                                                     @RequestBody List<RegistroNovoRequest> registros) {

        log.info("Atualizando Ponto - {} - {} - registros size: {}", matricula, paraStringData(dia), registros.size());
        var ponto = pontoService.adicionaRegistros(matricula, dia, registros.stream().
                map(r->r.toModel(null)).toList());
        return ResponseEntity.ok(PontoResponse.of(ponto));
    }

}
