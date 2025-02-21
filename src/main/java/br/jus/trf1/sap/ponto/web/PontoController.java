package br.jus.trf1.sap.ponto.web;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoService;
import br.jus.trf1.sap.ponto.web.dto.PontoResponse;
import br.jus.trf1.sap.ponto.web.dto.UsuariosPontoResponse;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.jus.trf1.sap.util.DataTempoUtil.*;

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
    public ResponseEntity<PontoResponse> criarPonto(@PathVariable Integer matricula, @PathVariable String dia) {

        log.info("Criando Ponto - {} - {}", matricula, dia);
        var ponto = pontoService.salvarPontoPorMatriculaMaisData(matricula, criaLocalDate(dia));
        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{matricula}/{dia}")
                .buildAndExpand(matricula, dia).

                toUri();

        return ResponseEntity.created(uriResponse).body(PontoResponse.of(ponto));
    }


    @PostMapping("/{dia}")
    public ResponseEntity<List<UsuariosPontoResponse>> criarPontos(@PathVariable String dia) {
        var usuariosPonto = new ArrayList<UsuariosPontoResponse>();
        log.info("Criando Pontos do dia {} ", dia);
        vinculoRepository.findAll().forEach(vinculo -> {
            var ponto = pontoService.salvarPontoDeUmVinculoPorData(vinculo, criaLocalDate(dia));
            usuariosPonto.add(new UsuariosPontoResponse(vinculo.getNome(), ponto.getId().toString()));
        });


        return ResponseEntity.status(HttpStatus.CREATED).body(usuariosPonto);
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
