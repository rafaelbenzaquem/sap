package br.jus.trf1.sipe.registro.web;

import br.jus.trf1.sipe.registro.RegistroService;
import br.jus.trf1.sipe.registro.web.dto.RegistroResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.HATEOASUtil.addLinksHATEOAS;
import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/sipe/registros")
public class RegistroDeleteController {

    private final RegistroService registroService;

    public RegistroDeleteController(RegistroService registroService) {
        this.registroService = registroService;
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<RegistroResponse>> apagar(@PathVariable("id") Long id) {
        var registro = registroService.apagar(id);
        var registroModel = EntityModel.of(RegistroResponse.of(registro));
        return ResponseEntity.ok(registroModel);
    }

}
