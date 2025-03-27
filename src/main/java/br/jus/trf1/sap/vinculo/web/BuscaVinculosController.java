package br.jus.trf1.sap.vinculo.web;

import br.jus.trf1.sap.vinculo.Vinculo;
import br.jus.trf1.sap.vinculo.VinculoService;
import br.jus.trf1.sap.vinculo.web.dto.VinculoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/sap/vinculos")
public class BuscaVinculosController {

    private final VinculoService service;

    public BuscaVinculosController(VinculoService service) {
        this.service = service;
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<VinculoResponse>> buscaVinculo(@PathVariable("id") Integer id) {
        var vinculo = service.buscaPorId(id);
        return ResponseEntity.ok(EntityModel.of(vinculo.toResponse(),
                linkTo(methodOn(BuscaVinculosController.class).buscaVinculo(vinculo.getId())).withSelfRel(),
                linkTo(methodOn(ApagaVinculosController.class).apagaVinculo(vinculo.getId())).withRel("delete")
        ));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<VinculoResponse>>> listarVinculos(@RequestParam(required = false)
                                                                                   String nome,
                                                                                   @RequestParam(required = false)
                                                                                   String cracha,
                                                                                   @RequestParam(required = false)
                                                                                   String matricula,
                                                                                   @RequestParam(defaultValue = "0")
                                                                                   int page,
                                                                                   @RequestParam(defaultValue = "5")
                                                                                   int size) {
        if (nome == null && cracha == null && matricula == null) {
            var vinculosPage = service.listar(PageRequest.of(page, size));
            // Adiciona links HATEOAS
            var pagedModel = addLinksHATEOASCrud(vinculosPage);
            addLinksPaginacao(vinculosPage,pagedModel,page, size);
            return ResponseEntity.ok(pagedModel);
        }
        var vinculosPage = service.buscarVinculosPorNomeOuCrachaOuMatricula(nome, cracha, matricula,
                PageRequest.of(page, size));
        var pagedModel = addLinksHATEOASCrud(vinculosPage);
        return ResponseEntity.ok(pagedModel);
    }

    private void addLinksPaginacao(Page<Vinculo> vinculosPage,
                                   PagedModel<EntityModel<VinculoResponse>> pagedModel,
                                   int page, int size) {
        // Links para paginação
        if (vinculosPage.hasPrevious()) {
            pagedModel.add(Link.of(
                    linkTo(methodOn(BuscaVinculosController.class)
                            .listarVinculos(null, null, null,
                                    page - 1, size)).toString(), "prev"));
        }
        if (vinculosPage.hasNext()) {
            pagedModel.add(Link.of(
                    linkTo(methodOn(BuscaVinculosController.class)
                            .listarVinculos(null, null, null,
                                    page + 1, size)).toString(), "next"));
        }
    }

    private PagedModel<EntityModel<VinculoResponse>> addLinksHATEOASCrud(Page<Vinculo> vinculosPage) {
        return PagedModel.of(
                vinculosPage.getContent().stream()
                        .map(vinculo -> EntityModel.of(vinculo.toResponse(),
                                linkTo(methodOn(BuscaVinculosController.class).buscaVinculo(vinculo.getId())).withSelfRel(),
                                linkTo(methodOn(ApagaVinculosController.class).apagaVinculo(vinculo.getId())).withRel("delete")
                        )).toList(),
                new PagedModel.PageMetadata(
                        vinculosPage.getSize(),
                        vinculosPage.getNumber(),
                        vinculosPage.getTotalElements(),
                        vinculosPage.getTotalPages())
        );
    }

}
