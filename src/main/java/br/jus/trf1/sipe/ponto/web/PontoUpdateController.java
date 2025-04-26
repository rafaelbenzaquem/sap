package br.jus.trf1.sipe.ponto.web;

import br.jus.trf1.sipe.ponto.IndicePonto;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.ponto.web.dto.PontoAtualizadoRequest;
import br.jus.trf1.sipe.ponto.web.dto.PontoAtualizadoResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sipe.comum.util.DataTempoUtil.paraString;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/v1/sipe/pontos")
public class PontoUpdateController {

    private final PontoService pontoService;

    public PontoUpdateController(PontoService pontoService) {
        this.pontoService = pontoService;
    }


    @PutMapping
    public ResponseEntity<EntityModel<PontoAtualizadoResponse>> atualizaPonto(@RequestBody
                                                                              @Valid
                                                                              PontoAtualizadoRequest
                                                                                      pontoAtualizadoRequest) {
        log.info("atualizaPonto - {}", pontoAtualizadoRequest);
        var dia = pontoAtualizadoRequest.dia();
        var diaFormatado = paraString(dia, PADRAO_ENTRADA_DATA);
        var matricula = pontoAtualizadoRequest.matricula();
        var indice = pontoAtualizadoRequest.indice();
        var descricao = pontoAtualizadoRequest.descricao();

        var ponto = pontoService.buscaPonto(matricula, dia);

        ponto.setDescricao(descricao);
        ponto.setIndice(IndicePonto.toEnum(indice));


        var uri = ServletUriComponentsBuilder.fromCurrentContextPath().
                path("/v1/sap/registros/pontos?matricula={matricula}&dia={dia}").
                buildAndExpand(matricula, diaFormatado).toUri();
        var pontoModel = EntityModel.of(PontoAtualizadoResponse.of(ponto),
                linkTo(methodOn(PontoReadController.class).buscaPonto(matricula, dia)).withSelfRel(),
                Link.of(uri.toString()).withRel("registros")
        );

        return ResponseEntity.created(uri).body(pontoModel);
    }

}
