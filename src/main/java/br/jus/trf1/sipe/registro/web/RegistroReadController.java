package br.jus.trf1.sipe.registro.web;

import br.jus.trf1.sipe.registro.RegistroService;
import br.jus.trf1.sipe.registro.web.dto.RegistroResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;
import static br.jus.trf1.sipe.comum.util.HATEOASUtil.addLinksHATEOAS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/sipe/registros")
public class RegistroReadController {

    private final RegistroService registroService;

    public RegistroReadController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @GetMapping(value = "/pontos")
    public ResponseEntity<CollectionModel<EntityModel<RegistroResponse>>> listarRegistrosDoPonto(@RequestParam
                                                                                                 String matricula,
                                                                                                 @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                 @RequestParam LocalDate dia) {
        var registros = registroService.listarRegistrosPonto(matricula, dia);

        return ResponseEntity.ok(addLinksHATEOAS(registros));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<RegistroResponse>> buscaRegistro(@PathVariable("id") Long id) {
        var registro = registroService.buscaRegistroPorId(id);
        var registroModel = EntityModel.of(RegistroResponse.of(registro),
                linkTo(methodOn(RegistroReadController.class).buscaRegistro(registro.getId())).withSelfRel());
        return ResponseEntity.ok(registroModel);
    }

}
