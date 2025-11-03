package br.jus.trf1.sipe.usuario.web;

import br.jus.trf1.sipe.servidor.ServidorService;
import br.jus.trf1.sipe.usuario.Usuario;
import br.jus.trf1.sipe.usuario.UsuarioService;
import br.jus.trf1.sipe.usuario.web.dto.UsuarioResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/v1/sipe/usuarios")
public class UsuarioReadController {

    private final UsuarioService usuarioService;
    private final ServidorService servidorService;

    public UsuarioReadController(UsuarioService usuarioService, ServidorService servidorService) {
        this.usuarioService = usuarioService;
        this.servidorService = servidorService;
    }

    @GetMapping(value = "/{matricula}")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<EntityModel<UsuarioResponse>> buscaUsuario(@PathVariable("matricula") String matricula) {
        var usuario = usuarioService.buscaPorMatricula(matricula);
        return ResponseEntity.ok(EntityModel.of(usuario.toResponse(),
                linkTo(methodOn(UsuarioReadController.class).buscaUsuario(matricula)).withSelfRel(),
                linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(usuario.getId())).withRel("delete")
        ));
    }

    @GetMapping("/pag")
    @PreAuthorize("hasAnyAuthority('GRP_SIPE_ADMIN', 'GRP_SIPE_RH', 'GRP_SIPE_DIRETOR')")
    public ResponseEntity<PagedModel<EntityModel<UsuarioResponse>>> paginarUsuarios(@RequestParam(required = false)
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
            log.info("Pagina listaUsuarios All");
            var usuarioPag = servidorService.paginar(PageRequest.of(page, size));
            // Adiciona links HATEOAS
            var pagedModel = addLinksHATEOASCrud(usuarioPag);
            addLinksPaginacao(usuarioPag, pagedModel, page, size);
            return ResponseEntity.ok(pagedModel);
        }


        var isNumerico = cracha.matches("\\d{3,16}");

        log.info("Pagina listaUsuarios filtered");
        var usuarioPag = servidorService.paginar(nome, isNumerico ? Integer.parseInt(cracha) : 0, matricula,
                PageRequest.of(page, size));
        var pagedModel = addLinksHATEOASCrud(usuarioPag);
        return ResponseEntity.ok(pagedModel);
    }


    @GetMapping
    @PreAuthorize("hasAnyAuthority('GRP_SIPE_ADMIN', 'GRP_SIPE_RH', 'GRP_SIPE_DIRETOR')")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioResponse>>> listarUsuarios(@RequestParam(required = false)
                                                                                        String nome,
                                                                                        @RequestParam(required = false)
                                                                                        String cracha,
                                                                                        @RequestParam(required = false)
                                                                                        String matricula
    ) {
        if (nome == null && cracha == null && matricula == null) {
            log.info("Lista listaUsuarios All");
            var usuarioPag = servidorService.listar();
            if (usuarioPag.isEmpty()) {
                return ResponseEntity.ok(CollectionModel.empty());
            }
            // Adiciona links HATEOAS
            var pagedModel = usuarioPag.stream().map(usuario -> {
                return EntityModel.of(usuario.toResponse(),
                        linkTo(methodOn(UsuarioReadController.class).buscaUsuario(usuario.getMatricula())).withSelfRel(),
                        linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(usuario.getId())).withRel("delete")
                );
            }).toList();

            return ResponseEntity.ok(CollectionModel.of(pagedModel));
        }

        var isNumerico = cracha.matches("\\d{3,16}");

        log.info("Lista listaUsuarios filtered");
        var usuarioPag = servidorService.listar(nome, isNumerico ? Integer.parseInt(cracha) : 0, matricula);
        // Adiciona links HATEOAS
        var pagedModel = usuarioPag.stream().map(usuario -> {
            return EntityModel.of(usuario.toResponse(),
                    linkTo(methodOn(UsuarioReadController.class).buscaUsuario(usuario.getMatricula())).withSelfRel(),
                    linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(usuario.getId())).withRel("delete")
            );
        }).toList();

        return ResponseEntity.ok(CollectionModel.of(pagedModel));
    }

    private void addLinksPaginacao(Page<? extends Usuario> usuarioPag,
                                   PagedModel<EntityModel<UsuarioResponse>> pagedModel,
                                   int page, int size) {
        // Links para paginação
        if (usuarioPag.hasPrevious()) {
            pagedModel.add(Link.of(
                    linkTo(methodOn(UsuarioReadController.class)
                            .paginarUsuarios(null, null, null,
                                    page - 1, size)).toString(), "prev"));
        }
        if (usuarioPag.hasNext()) {
            pagedModel.add(Link.of(
                    linkTo(methodOn(UsuarioReadController.class)
                            .paginarUsuarios(null, null, null,
                                    page + 1, size)).toString(), "next"));
        }
    }

    private PagedModel<EntityModel<UsuarioResponse>> addLinksHATEOASCrud(Page<? extends Usuario> usuarioPage) {
        return PagedModel.of(
                usuarioPage.getContent().stream()
                        .map(usuario -> EntityModel.of(usuario.toResponse(),
                                linkTo(methodOn(UsuarioReadController.class).buscaUsuario(usuario.getMatricula())).withSelfRel(),
                                linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(usuario.getId())).withRel("delete")
                        )).toList(),
                new PagedModel.PageMetadata(
                        usuarioPage.getSize(),
                        usuarioPage.getNumber(),
                        usuarioPage.getTotalElements(),
                        usuarioPage.getTotalPages())
        );
    }

}
