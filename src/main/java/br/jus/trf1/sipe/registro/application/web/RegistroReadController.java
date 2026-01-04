package br.jus.trf1.sipe.registro.application.web;

import br.jus.trf1.sipe.registro.application.web.dto.RegistroResponse;
import br.jus.trf1.sipe.registro.domain.port.in.RegistroServicePort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sipe.comum.util.HATEOASUtil.addLinksHATEOAS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/sipe/registros")
public class RegistroReadController {

    private final RegistroServicePort registroServicePort;

    public RegistroReadController(RegistroServicePort registroServicePort) {
        this.registroServicePort = registroServicePort;
    }

    @GetMapping(value = "/pontos")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<CollectionModel<EntityModel<RegistroResponse>>> listarRegistrosDoPonto(@RequestParam
                                                                                                 String matricula,
                                                                                                 @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                 @RequestParam
                                                                                                 LocalDate dia,
                                                                                                 @RequestParam(required = false, defaultValue = "false")
                                                                                                 boolean todos) {
        var registros = registroServicePort.listaAtuaisDoPonto(matricula, dia, todos);
        return ResponseEntity.ok(addLinksHATEOAS(registros));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<EntityModel<RegistroResponse>> buscaRegistro(@PathVariable("id") Long id) {
        var registro = registroServicePort.buscaRegistroPorId(id);
        var registroModel = EntityModel.of(RegistroResponse.of(registro),
                linkTo(methodOn(RegistroReadController.class).buscaRegistro(registro.getId())).withSelfRel());
        return ResponseEntity.ok(registroModel);
    }

}
