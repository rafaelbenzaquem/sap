package br.jus.trf1.sipe.usuario.web;

import br.jus.trf1.sipe.usuario.Usuario;
import br.jus.trf1.sipe.usuario.UsuarioService;
import br.jus.trf1.sipe.usuario.web.dto.UsuarioResponse;
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
@RequestMapping(value = "/v1/sap/usuarios")
public class UsuarioReadController {

    private final UsuarioService service;

    public UsuarioReadController(UsuarioService service) {
        this.service = service;
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<UsuarioResponse>> buscaVinculo(@PathVariable("id") Integer id) {
        var vinculo = service.buscaPorId(id);
        return ResponseEntity.ok(EntityModel.of(vinculo.toResponse(),
                linkTo(methodOn(UsuarioReadController.class).buscaVinculo(vinculo.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(vinculo.getId())).withRel("delete")
        ));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<UsuarioResponse>>> listarVinculos(@RequestParam(required = false)
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

    private void addLinksPaginacao(Page<Usuario> vinculosPage,
                                   PagedModel<EntityModel<UsuarioResponse>> pagedModel,
                                   int page, int size) {
        // Links para paginação
        if (vinculosPage.hasPrevious()) {
            pagedModel.add(Link.of(
                    linkTo(methodOn(UsuarioReadController.class)
                            .listarVinculos(null, null, null,
                                    page - 1, size)).toString(), "prev"));
        }
        if (vinculosPage.hasNext()) {
            pagedModel.add(Link.of(
                    linkTo(methodOn(UsuarioReadController.class)
                            .listarVinculos(null, null, null,
                                    page + 1, size)).toString(), "next"));
        }
    }

    private PagedModel<EntityModel<UsuarioResponse>> addLinksHATEOASCrud(Page<Usuario> vinculosPage) {
        return PagedModel.of(
                vinculosPage.getContent().stream()
                        .map(vinculo -> EntityModel.of(vinculo.toResponse(),
                                linkTo(methodOn(UsuarioReadController.class).buscaVinculo(vinculo.getId())).withSelfRel(),
                                linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(vinculo.getId())).withRel("delete")
                        )).toList(),
                new PagedModel.PageMetadata(
                        vinculosPage.getSize(),
                        vinculosPage.getNumber(),
                        vinculosPage.getTotalElements(),
                        vinculosPage.getTotalPages())
        );
    }

}
