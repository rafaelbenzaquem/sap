package br.jus.trf1.sipe.ponto.application.web;

import br.jus.trf1.sipe.ponto.domain.port.in.PontoServicePort;
import br.jus.trf1.sipe.ponto.application.web.dto.PontoNovoRequest;
import br.jus.trf1.sipe.ponto.application.web.dto.PontoNovoResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sipe.comum.util.DataTempoUtil.paraString;
import static br.jus.trf1.sipe.comum.util.HATEOASUtil.addLinksHATEOAS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/v1/sipe/pontos")
public class PontoCreateController {

    private final PontoServicePort pontoServicePort;

    public PontoCreateController(PontoServicePort pontoServicePort) {
        this.pontoServicePort = pontoServicePort;
    }

    /**
     * Endpoint para criar um ponto a partir payload presente no corpo de uma requisição da api restfull
     *
     * @param pontoNovoRequest Classe que representa o payload de entrada da requisição
     * @return Payload de saída formatada para restfull, contendo o ponto criado
     */
    @PostMapping
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<EntityModel<PontoNovoResponse>> criaPonto(@RequestBody
                                                                    @Valid
                                                                    PontoNovoRequest pontoNovoRequest) {
        log.info("CriaPonto - {}", pontoNovoRequest);
        var dia = pontoNovoRequest.dia();
        var diaFormatado = paraString(dia, PADRAO_ENTRADA_DATA);
        var matricula = pontoNovoRequest.matricula();
        var ponto = PontoWebMapper.toDomain(pontoNovoRequest);
         ponto = pontoServicePort.criaPonto(ponto);

        var uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{matricula}/{diaFormatado}")
                .buildAndExpand(matricula, diaFormatado).
                toUri();

        var pontoModel = EntityModel.of(PontoWebMapper.toNovoResponse(ponto),
                linkTo(methodOn(PontoReadController.class).buscaPonto(matricula, dia)).withSelfRel()
        );
        return ResponseEntity.created(uri).body(pontoModel);
    }

}
