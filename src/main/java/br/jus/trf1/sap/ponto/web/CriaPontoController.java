package br.jus.trf1.sap.ponto.web;

import br.jus.trf1.sap.externo.coletor.historico.HistoricoService;
import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoService;
import br.jus.trf1.sap.ponto.web.dto.PontoResponse;
import br.jus.trf1.sap.ponto.web.dto.UsuariosPontoResponse;
import br.jus.trf1.sap.vinculo.Vinculo;
import br.jus.trf1.sap.vinculo.VinculoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static br.jus.trf1.sap.util.ConstantesDataTempoUtil.PADRAO_ENTRADA_DATA;

@Slf4j
@RestController
@RequestMapping("/v1/sap/pontos")
public class CriaPontoController {

    private final PontoService pontoService;
    private final VinculoService vinculoService;
    private final HistoricoService historicoService;


    public CriaPontoController(PontoService pontoService, VinculoService vinculoService, HistoricoService historicoService) {
        this.pontoService = pontoService;
        this.vinculoService = vinculoService;
        this.historicoService = historicoService;
    }

    @PostMapping("/{matricula}/{dia}")
    public ResponseEntity<PontoResponse> criaPonto(@PathVariable
                                                   Integer matricula,
                                                   @PathVariable
                                                   @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                   LocalDate dia) {

        log.info("Criando Ponto - {} - {}", matricula, dia);
        var vinculo = vinculoService.buscaPorMatricula(matricula);
        var historicos = historicoService.buscarHistoricoDeAcesso(dia, null,
                vinculo.getCracha(), null, null);
        var ponto = pontoService.salvaPontoPorMatriculaMaisData(matricula, dia, historicos);
        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{matricula}/{dia}")
                .buildAndExpand(matricula, dia).
                toUri();

        return ResponseEntity.created(uriResponse).body(PontoResponse.of(ponto));
    }

    @PostMapping("/{dia}")
    public ResponseEntity<List<UsuariosPontoResponse>> criaPontosDoDia(@PathVariable
                                                                       @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                       LocalDate dia) {
        var usuariosPonto = new ArrayList<UsuariosPontoResponse>();
        log.info("Criando Pontos do dia {} ", dia);
        vinculoService.listar().forEach(vinculo -> {
            var historicos = historicoService.buscarHistoricoDeAcesso(dia, null,
                    vinculo.getCracha(), null, null);
            var ponto = pontoService.salvaPontoPorData(vinculo.getMatricula(), dia, historicos);
            usuariosPonto.add(new UsuariosPontoResponse(vinculo.getNome(), ponto.getId().toString()));
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(usuariosPonto);
    }

    @PostMapping()
    public ResponseEntity<List<UsuariosPontoResponse>> criarPontosDeTodosOsVinculosNoPeriodo(@RequestParam("inicio")
                                                                                             @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                             LocalDate inicio,
                                                                                             @RequestParam("fim")
                                                                                             @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                             LocalDate fim) {
        log.info("Criando Pontos de - {} - {}", inicio, fim);
        var usuariosPonto = new ArrayList<UsuariosPontoResponse>();
        vinculoService.listar().forEach(vinculo -> {
            List<Ponto> pontos = pontoService.carregaPontosPorPeriodo(vinculo, inicio, fim,
                    historicoService);
            usuariosPonto.add(new UsuariosPontoResponse(vinculo.getNome(),
                    "Quantidas de pontos: " + pontos.size()));
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(usuariosPonto);
    }


    @PostMapping("/{matricula}/servidor")
    public ResponseEntity<UsuariosPontoResponse> criarPontosPorPeriodo(@PathVariable
                                                                       Integer matricula,
                                                                       @RequestParam("inicio")
                                                                       @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                       LocalDate inicio,
                                                                       @RequestParam("fim")
                                                                       @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                       LocalDate fim) {
        log.info("Criando Pontos de - {} - {}", inicio, fim);
        Vinculo vinculo = vinculoService.buscaPorMatricula(matricula);

        List<Ponto> pontos = pontoService.carregaPontosPorPeriodo(vinculo, inicio, fim,
                historicoService);
        var usuarioPonto = new UsuariosPontoResponse(vinculo.getNome(),
                "Quantidas de pontos: " + pontos.size());

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioPonto);
    }

}
