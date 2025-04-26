package br.jus.trf1.sipe.ponto.web;

import br.jus.trf1.sipe.externo.coletor.historico.HistoricoService;
import br.jus.trf1.sipe.externo.coletor.historico.dto.HistoricoResponse;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.ponto.exceptions.PontoExistenteException;
import br.jus.trf1.sipe.ponto.web.dto.PontoNovoRequest;
import br.jus.trf1.sipe.ponto.web.dto.PontoNovoResponse;
import br.jus.trf1.sipe.registro.web.dto.RegistroNovoRequest;
import br.jus.trf1.sipe.usuario.Usuario;
import br.jus.trf1.sipe.usuario.UsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_SAIDA_DATA;
import static br.jus.trf1.sipe.comum.util.DataTempoUtil.paraString;
import static br.jus.trf1.sipe.comum.util.HATEOASUtil.addLinksHATEOAS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/v1/sipe/pontos")
public class PontoCreateController {

    private final PontoService pontoService;
    private final UsuarioService usuarioService;
    private final HistoricoService historicoService;

    public PontoCreateController(PontoService pontoService,
                                 UsuarioService usuarioService,
                                 HistoricoService historicoService) {
        this.pontoService = pontoService;
        this.usuarioService = usuarioService;
        this.historicoService = historicoService;
    }

    /**
     * Endpoint para criar um ponto a partir payload presente no corpo de uma requisição da api restfull
     *
     * @param pontoNovoRequest Classe que representa o payload de entrada da requisição
     * @return Payload de saída formatada para restfull, contendo o ponto criado
     */
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

    /**
     * Ponto criado automáticamente(buscando o histórico de acessos do usuário no sistema de acesso)
     * a partir do endpoint '/pontos/usuarios/{matricula}'
     *
     * @param matricula Matrícula do usuário que representa parte do identificador de ponto
     * @param dia       LocalDate que representa a data do ponto, parte do identificador de ponto
     * @return Payload de saída formatada para restfull, contendo o ponto criado
     */
    @PostMapping("/usuarios/{matricula}")
    public ResponseEntity<EntityModel<PontoNovoResponse>> criarPontoAutomatico(@PathVariable
                                                                               String matricula,
                                                                               @RequestParam
                                                                               @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                               LocalDate dia) {
        var stringDia = paraString(dia, PADRAO_SAIDA_DATA);
        log.info("Criando Ponto - matricula: {}, dia - {}", matricula, stringDia);
        if (pontoService.existe(matricula, dia)) {
            throw new PontoExistenteException(matricula, dia);
        }

        var usuario = usuarioService.buscaPorMatricula(matricula);
        var historicos = historicoService.buscarHistoricoDeAcesso(dia, null,
                usuario.getCracha(), null, null);

        var ponto = pontoService.salvaPonto(matricula, dia, historicos.stream().map(HistoricoResponse::toModel).toList());
        var uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{matricula}/{dia}")
                .buildAndExpand(matricula, dia).
                toUri();
        var pontoModel = EntityModel.of(PontoNovoResponse.of(ponto),
                linkTo(methodOn(PontoReadController.class).buscaPonto(matricula, dia)).withSelfRel(),
                Link.of(uri.toString()).withRel("registros")
        );
        return ResponseEntity.created(uri).body(pontoModel);
    }

    /**
     * Cria pontos no período (início e fim) para um usuário dado sua matrícula, preenchendo os pontos com os registros
     * proveniente do histórico do sistema de controle de acesso
     *
     * @param matricula Matrícula do usuário que representa parte do identificador de ponto
     * @param inicio    LocaDate que representa o início do período, não pode ser nulo
     * @param fim       LocalDate que representa o fim do período, pode ser nulo
     * @return Coleção com pontos criados formatado para restfull
     */
    @PostMapping("/usuarios")
    public ResponseEntity<CollectionModel<EntityModel<PontoNovoResponse>>> criarPontosPorPeriodo(@RequestParam
                                                                                                 String matricula,
                                                                                                 @RequestParam
                                                                                                 @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                 LocalDate inicio,
                                                                                                 @RequestParam
                                                                                                 @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                 LocalDate fim) {
        log.info("Criando Pontos de - {} - {}", paraString(inicio), paraString(fim));
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim.");
        }
        Usuario usuario = usuarioService.buscaPorMatricula(matricula);

        List<Ponto> pontos = pontoService.carregaPontos(usuario, inicio, fim, historicoService);

        return ResponseEntity.status(HttpStatus.CREATED).body(addLinksHATEOAS(inicio, fim, pontos));
    }


}
