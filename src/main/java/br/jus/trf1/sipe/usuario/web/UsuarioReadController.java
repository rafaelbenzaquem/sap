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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/sipe/usuarios")
public class UsuarioReadController {

    private final UsuarioService service;

    public UsuarioReadController(UsuarioService service) {
        this.service = service;
    }


    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<EntityModel<UsuarioResponse>> buscaVinculo(@PathVariable("id") Integer id) {
        var vinculo = service.buscaPorId(id);
        return ResponseEntity.ok(EntityModel.of(vinculo.toResponse(),
                linkTo(methodOn(UsuarioReadController.class).buscaVinculo(vinculo.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(vinculo.getId())).withRel("delete")
        ));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
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
            var usuarioPag = service.listar(PageRequest.of(page, size));
            // Adiciona links HATEOAS
            var pagedModel = addLinksHATEOASCrud(usuarioPag);
            addLinksPaginacao(usuarioPag,pagedModel,page, size);
            return ResponseEntity.ok(pagedModel);
        }
        var usuarioPag = service.buscarVinculosPorNomeOuCrachaOuMatricula(nome, cracha, matricula,
                PageRequest.of(page, size));
        var pagedModel = addLinksHATEOASCrud(usuarioPag);
        return ResponseEntity.ok(pagedModel);
    }

    private void addLinksPaginacao(Page<Usuario> usuarioPag,
                                   PagedModel<EntityModel<UsuarioResponse>> pagedModel,
                                   int page, int size) {
        // Links para paginação
        if (usuarioPag.hasPrevious()) {
            pagedModel.add(Link.of(
                    linkTo(methodOn(UsuarioReadController.class)
                            .listarVinculos(null, null, null,
                                    page - 1, size)).toString(), "prev"));
        }
        if (usuarioPag.hasNext()) {
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
