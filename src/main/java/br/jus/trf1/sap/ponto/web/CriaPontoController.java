package br.jus.trf1.sap.ponto.web;

import br.jus.trf1.sap.comum.util.DataTempoUtil;
import br.jus.trf1.sap.externo.coletor.historico.HistoricoService;
import br.jus.trf1.sap.externo.coletor.historico.dto.HistoricoResponse;
import br.jus.trf1.sap.externo.jsarh.servidor.ServidorService;
import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoId;
import br.jus.trf1.sap.ponto.PontoService;
import br.jus.trf1.sap.ponto.exceptions.PontoExistenteException;
import br.jus.trf1.sap.ponto.web.dto.PontoNovoRequest;
import br.jus.trf1.sap.ponto.web.dto.PontoResponse;
import br.jus.trf1.sap.ponto.web.dto.UsuariosPontoResponse;
import br.jus.trf1.sap.vinculo.Vinculo;
import br.jus.trf1.sap.vinculo.VinculoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static br.jus.trf1.sap.comum.util.ConstantesDataTempoUtil.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sap.comum.util.ConstantesDataTempoUtil.PADRAO_SAIDA_DATA;
import static br.jus.trf1.sap.comum.util.DataTempoUtil.paraString;

@Slf4j
@RestController
@RequestMapping("/v1/sap/pontos")
public class CriaPontoController {

    private final PontoService pontoService;
    private final VinculoService vinculoService;
    private final HistoricoService historicoService;
    private final ServidorService servidorService;

    public CriaPontoController(PontoService pontoService,
                               VinculoService vinculoService,
                               HistoricoService historicoService,
                               ServidorService servidorService) {
        this.pontoService = pontoService;
        this.vinculoService = vinculoService;
        this.historicoService = historicoService;
        this.servidorService = servidorService;
    }


    @PostMapping
    public ResponseEntity<PontoResponse> criaPonto(@RequestBody @Valid PontoNovoRequest pontoRequest) {
        log.info("criaPonto - {}", pontoRequest);
        var dia = pontoRequest.dia();
        var matricula = pontoRequest.matricula();
        if (pontoService.existe(matricula, dia)) {
            throw new PontoExistenteException(Ponto.builder().
                    id(PontoId.builder().
                            matricula(matricula).
                            dia(dia).
                            build()).
                    build());
        }
        var id = PontoId.builder().
                dia(dia).
                matricula(matricula).
                build();
        var registros = pontoRequest.registros().
                stream().map(rnr -> rnr.toModel(Ponto.builder().
                        id(id).
                        build())).
                toList();

        var ponto = pontoService.salvaPonto(matricula, dia, registros);
        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{matricula}/{dia}")
                .buildAndExpand(matricula, pontoRequest.dia()).
                toUri();

        return ResponseEntity.created(uriResponse).body(PontoResponse.of(ponto));
    }

    @PostMapping("/{matricula}/{dia}")
    public ResponseEntity<PontoResponse> criarPonto(@PathVariable
                                                    String matricula,
                                                    @PathVariable
                                                    @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                    LocalDate dia) {
        var stringDia = paraString(dia, PADRAO_SAIDA_DATA);
        log.info("Criando Ponto - matricula: {}, dia - {}", matricula, stringDia);
        var vinculo = vinculoService.buscaPorMatricula(matricula);
        var historicos = historicoService.buscarHistoricoDeAcesso(dia, null,
                vinculo.getCracha(), null, null);
        if (pontoService.existe(matricula, dia)) {
            throw new PontoExistenteException(Ponto.builder().
                    id(PontoId.builder().
                            matricula(matricula).
                            dia(dia).
                            build()).
                    build());
        }
        var ponto = pontoService.salvaPonto(matricula, dia, historicos.stream().map(HistoricoResponse::toModel).toList());
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
        log.info("Criando Pontos do dia {} ", DataTempoUtil.paraString(dia));
        vinculoService.listar().forEach(v -> {
            var historicos = historicoService.buscarHistoricoDeAcesso(dia, null,
                    v.getCracha(), null, null);
            var ponto = pontoService.salvaPonto(
                    v.getMatricula(),
                    dia,
                    historicos.stream().map(HistoricoResponse::toModel).toList());
            usuariosPonto.add(new UsuariosPontoResponse(v.getNome(), ponto.getId().toString()));
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(usuariosPonto);
    }

    @PostMapping("/vinculos")
    public ResponseEntity<List<UsuariosPontoResponse>> criarPontosDeTodosOsVinculosNoPeriodo(@RequestParam("inicio")
                                                                                             @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                             LocalDate inicio,
                                                                                             @RequestParam("fim")
                                                                                             @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                             LocalDate fim) {
        log.info("Criando Pontos de - {} - {}", DataTempoUtil.paraString(inicio), DataTempoUtil.paraString(fim));
        var usuariosPonto = new ArrayList<UsuariosPontoResponse>();
        vinculoService.listar().forEach(vinculo -> {
            var optServidor = servidorService.buscaDadosServidor(vinculo.getMatricula());
            if (optServidor.isPresent()) {
                List<Ponto> pontos = pontoService.carregaPontos(vinculo, inicio, fim,
                        historicoService);
                usuariosPonto.add(new UsuariosPontoResponse(vinculo.getNome(),
                        "Quantidas de pontos: " + pontos.size()));
            }
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(usuariosPonto);
    }


    @PostMapping("/{matricula}/servidor")
    public ResponseEntity<UsuariosPontoResponse> criarPontosPorPeriodo(@PathVariable
                                                                       String matricula,
                                                                       @RequestParam("inicio")
                                                                       @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                       LocalDate inicio,
                                                                       @RequestParam("fim")
                                                                       @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                       LocalDate fim) {
        log.info("Criando Pontos de - {} - {}", DataTempoUtil.paraString(inicio), DataTempoUtil.paraString(fim));
        Vinculo vinculo = vinculoService.buscaPorMatricula(matricula);

        List<Ponto> pontos = pontoService.carregaPontos(vinculo, inicio, fim,
                historicoService);
        var usuarioPonto = new UsuariosPontoResponse(vinculo.getNome(),
                "Quantidas de pontos: " + pontos.size());

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioPonto);
    }
}
