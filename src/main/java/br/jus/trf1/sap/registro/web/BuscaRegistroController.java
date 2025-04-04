package br.jus.trf1.sap.registro.web;

import br.jus.trf1.sap.registro.RegistroService;
import br.jus.trf1.sap.registro.web.dto.RegistroResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static br.jus.trf1.sap.comum.util.ConstantesDataTempoUtil.PADRAO_ENTRADA_DATA;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/sap/registros")
public class BuscaRegistroController {

    private final RegistroService registroService;

    public BuscaRegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @GetMapping(value = "/pontos")
    public ResponseEntity<CollectionModel<EntityModel<RegistroResponse>>> listarRegistrosDoPonto(@RequestParam
                                                                                                 String matricula,
                                                                                                 @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                                                                 @RequestParam LocalDate dia) {
        var registros = registroService.listarRegistrosPonto(matricula, dia);
        var registrosEntityModelList = registros.stream().map(registro ->
                EntityModel.of(
                        RegistroResponse.of(registro),
                        linkTo(methodOn(BuscaRegistroController.class).buscaRegistro(registro.getId())).withSelfRel()
                )
        ).toList();

        var registrosCollectionModel = CollectionModel.of(registrosEntityModelList,
                linkTo(methodOn(BuscaRegistroController.class).listarRegistrosDoPonto(matricula, dia)).withSelfRel()
        );

        return ResponseEntity.ok(registrosCollectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<RegistroResponse>> buscaRegistro(@PathVariable("id") Long id) {
        var registro = registroService.buscaRegistroPorId(id);
        var registroModel = EntityModel.of(RegistroResponse.of(registro),
                linkTo(methodOn(BuscaRegistroController.class).buscaRegistro(registro.getId())).withSelfRel());
        return ResponseEntity.ok(registroModel);
    }

}
