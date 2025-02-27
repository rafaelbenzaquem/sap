package br.jus.trf1.sap.ponto.web;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoService;
import br.jus.trf1.sap.ponto.web.dto.PontoResponse;
import br.jus.trf1.sap.ponto.web.dto.UsuariosPontoResponse;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.jus.trf1.sap.util.ConstantesDataTempoUtil.PADRAO_ENTRADA_DATA;

@Slf4j
@RestController
@RequestMapping("/v1/sap/pontos")
public class PontoController {

    private final PontoService pontoService;
    private final VinculoRepository vinculoRepository;

    public PontoController(PontoService pontoService, VinculoRepository vinculoRepository) {
        this.pontoService = pontoService;
        this.vinculoRepository = vinculoRepository;
    }


    @PostMapping("/{matricula}/{dia}")
    public ResponseEntity<PontoResponse> criarPonto(@PathVariable Integer matricula,
                                                    @PathVariable
                                                    @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA) LocalDate dia) {

        log.info("Criando Ponto - {} - {}", matricula, dia);
        var ponto = pontoService.salvarPontoPorMatriculaMaisData(matricula, dia);
        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{matricula}/{dia}")
                .buildAndExpand(matricula, dia).

                toUri();

        return ResponseEntity.created(uriResponse).body(PontoResponse.of(ponto));
    }


    @PostMapping("/{dia}")
    public ResponseEntity<List<UsuariosPontoResponse>> criarPontos(@PathVariable
                                                                   @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                   LocalDate dia) {
        var usuariosPonto = new ArrayList<UsuariosPontoResponse>();
        log.info("Criando Pontos do dia {} ", dia);
        vinculoRepository.findAll().forEach(vinculo -> {
            var ponto = pontoService.salvarPontoDeUmVinculoPorData(vinculo, dia);
            usuariosPonto.add(new UsuariosPontoResponse(vinculo.getNome(), ponto.getId().toString()));
        });


        return ResponseEntity.status(HttpStatus.CREATED).body(usuariosPonto);
    }

    @GetMapping("/{matricula}/{dia}")
    public ResponseEntity<PontoResponse> buscarPonto(@PathVariable Integer matricula,
                                                     @PathVariable
                                                     @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                     LocalDate dia) {

        log.info("Buscando Ponto - {} - {}", matricula, dia);
        Optional<Ponto> pontoOpt = pontoService.buscarPonto(matricula, dia);
        return pontoOpt.map(ponto -> ResponseEntity.ok(PontoResponse.of(ponto))).
                orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{matricula}/{dia}")
    public ResponseEntity<PontoResponse> buscarAtualizacaoPonto(@PathVariable Integer matricula,
                                                                @PathVariable
                                                                @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                LocalDate dia) {

        log.info("Atualizando Ponto - {} - {}", matricula, dia);
        Optional<Ponto> pontoOpt = pontoService.buscarAtualizarRegistrosPonto(matricula, dia);
        return pontoOpt.map(ponto -> ResponseEntity.ok(PontoResponse.of(ponto))).
                orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<List<PontoResponse>> buscarPontosPorIntervalosDatas(@PathVariable Integer matricula,
                                                                              @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                              LocalDate inicio,
                                                                              @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                              LocalDate fim) {

        var pontos = pontoService.buscarPontos(matricula, inicio, fim);
        return ResponseEntity.ok(pontos.stream().map(PontoResponse::of).toList());
    }

}
