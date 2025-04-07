package br.jus.trf1.sap.ponto.web;

import br.jus.trf1.sap.externo.coletor.historico.HistoricoService;
import br.jus.trf1.sap.externo.coletor.historico.dto.HistoricoResponse;
import br.jus.trf1.sap.externo.jsarh.servidor.ServidorService;
import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoService;
import br.jus.trf1.sap.ponto.exceptions.PontoExistenteException;
import br.jus.trf1.sap.ponto.web.dto.PontoNovoRequest;
import br.jus.trf1.sap.ponto.web.dto.PontoNovoResponse;
import br.jus.trf1.sap.ponto.web.dto.UsuariosPontoResponse;
import br.jus.trf1.sap.registro.web.dto.RegistroNovoRequest;
import br.jus.trf1.sap.usuario.Usuario;
import br.jus.trf1.sap.usuario.UsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/v1/sap/pontos")
public class PontoCreateController {

    private final PontoService pontoService;
    private final UsuarioService usuarioService;
    private final HistoricoService historicoService;
    private final ServidorService servidorService;

    public PontoCreateController(PontoService pontoService,
                                 UsuarioService usuarioService,
                                 HistoricoService historicoService,
                                 ServidorService servidorService) {
        this.pontoService = pontoService;
        this.usuarioService = usuarioService;
        this.historicoService = historicoService;
        this.servidorService = servidorService;
    }


    @PostMapping
    public ResponseEntity<EntityModel<PontoNovoResponse>> criaPontoManual(@RequestBody
                                                                          @Valid
                                                                          PontoNovoRequest pontoNovoRequest) {
        log.info("criaPonto - {}", pontoNovoRequest);
        var dia = pontoNovoRequest.dia();
        var diaFormatado = paraString(dia, PADRAO_ENTRADA_DATA);
        var matricula = pontoNovoRequest.matricula();

        var registros = pontoNovoRequest.registros().
                stream().map(RegistroNovoRequest::toModel).
                toList();

        var ponto = pontoService.salvaPonto(matricula, dia, registros);


        var uri = ServletUriComponentsBuilder.fromCurrentContextPath().
                path("/v1/sap/registros/pontos?matricula={matricula}&dia={dia}").
                buildAndExpand(matricula, diaFormatado).toUri();
        var pontoModel = EntityModel.of(PontoNovoResponse.of(ponto),
                linkTo(methodOn(PontoReadController.class).buscaPonto(matricula, dia)).withSelfRel(),
                Link.of(uri.toString()).withRel("registros")
        );

        return ResponseEntity.created(uri).body(pontoModel);
    }

    @PostMapping("/{matricula}/{dia}")
    public ResponseEntity<EntityModel<PontoNovoResponse>> criarPontoAutomatico(@PathVariable
                                                                  String matricula,
                                                                  @PathVariable
                                                                  @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                  LocalDate dia) {
        var stringDia = paraString(dia, PADRAO_SAIDA_DATA);
        log.info("Criando Ponto - matricula: {}, dia - {}", matricula, stringDia);
        if (pontoService.existe(matricula, dia)) {
            throw new PontoExistenteException(matricula, dia);
        }

        var vinculo = usuarioService.buscaPorMatricula(matricula);
        var historicos = historicoService.buscarHistoricoDeAcesso(dia, null,
                vinculo.getCracha(), null, null);

        var ponto = pontoService.salvaPonto(matricula, dia, historicos.stream().map(HistoricoResponse::toModel).toList());
        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{matricula}/{dia}")
                .buildAndExpand(matricula, dia).
                toUri();
        var pontoModel = EntityModel.of(PontoNovoResponse.of(ponto),
                linkTo(methodOn(PontoReadController.class).buscaPonto(matricula, dia)).withSelfRel(),
                Link.of(uriResponse.toString()).withRel("registros")
        );
        return ResponseEntity.created(uriResponse).body(pontoModel);
    }

    @PostMapping("/{dia}")
    public ResponseEntity<List<UsuariosPontoResponse>> criaPontosDoDia(@PathVariable
                                                                       @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                       LocalDate dia) {
        var usuariosPonto = new ArrayList<UsuariosPontoResponse>();
        log.info("Criando Pontos do dia {} ", paraString(dia, PADRAO_SAIDA_DATA));
        usuarioService.listar().forEach(v -> {
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

    @PostMapping("/servidores")
    public ResponseEntity<List<UsuariosPontoResponse>> criarPontosDeTodosOsVinculosNoPeriodo(@RequestParam("inicio")
                                                                                             @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                             LocalDate inicio,
                                                                                             @RequestParam("fim")
                                                                                             @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                             LocalDate fim) {
        log.info("Criando pontos no período - {} - {}", paraString(inicio), paraString(fim));
        var usuariosPonto = new ArrayList<UsuariosPontoResponse>();
        usuarioService.listar().forEach(vinculo -> {
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


    @PostMapping("/{matricula}")
    public ResponseEntity<UsuariosPontoResponse> criarPontosPorPeriodo(@PathVariable
                                                                       String matricula,
                                                                       @RequestParam("inicio")
                                                                       @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                       LocalDate inicio,
                                                                       @RequestParam("fim")
                                                                       @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                       LocalDate fim) {
        log.info("Criando Pontos de - {} - {}", paraString(inicio), paraString(fim));
        Usuario usuario = usuarioService.buscaPorMatricula(matricula);

        List<Ponto> pontos = pontoService.carregaPontos(usuario, inicio, fim,
                historicoService);
        var usuarioPonto = new UsuariosPontoResponse(usuario.getNome(),
                "Quantidas de pontos: " + pontos.size());

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioPonto);
    }
}
