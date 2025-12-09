package br.jus.trf1.sipe.usuario.application.web;

import br.jus.trf1.sipe.servidor.domain.port.in.ServidorServicePort;
import br.jus.trf1.sipe.usuario.UsuarioMapper;
import br.jus.trf1.sipe.usuario.application.web.dto.UsuarioResponse;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.domain.port.in.UsuarioServicePort;
import br.jus.trf1.sipe.usuario.web.UsuarioDeleteController;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/v1/sipe/usuarios")
@RequiredArgsConstructor
public class UsuarioReadController {

    private final UsuarioServicePort usuarioService;
    private final ServidorServicePort servidorService; // Assumindo porta para ServidorService
    private final UsuarioMapper usuarioMapper;

    @GetMapping(value = "/{matricula}")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<EntityModel<UsuarioResponse>> buscaUsuario(@PathVariable("matricula") String matricula) {
        var usuario = usuarioService.buscaPorMatricula(matricula);
        return ResponseEntity.ok(EntityModel.of(usuarioMapper.toResponse(usuario),
                linkTo(methodOn(UsuarioReadController.class).buscaUsuario(matricula)).withSelfRel(),
                linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(usuario.getId())).withRel("delete")
        ));
    }

    @GetMapping("/pag")
    @PreAuthorize("hasAnyAuthority('GRP_SIPE_ADMIN', 'GRP_SIPE_RH', 'GRP_SIPE_DIRETOR')")
    public ResponseEntity<PagedModel<EntityModel<UsuarioResponse>>> paginarUsuarios(@RequestParam(required = false) String nome,
                                                                                    @RequestParam(required = false) String cracha,
                                                                                    @RequestParam(required = false) String matricula,
                                                                                    @RequestParam(defaultValue = "0") int page,
                                                                                    @RequestParam(defaultValue = "5") int size) {
        Page<? extends Usuario> usuarioPag;
        if (nome == null && cracha == null && matricula == null) {
            log.info("Pagina listaUsuarios All");
            usuarioPag = servidorService.paginar(PageRequest.of(page, size));
        } else {
            var isNumerico = cracha != null && cracha.matches("\\d{3,16}");
            log.info("Pagina listaUsuarios filtered");
            usuarioPag = servidorService.paginar(nome, isNumerico ? Integer.parseInt(cracha) : 0, matricula, PageRequest.of(page, size));
        }

        var pagedModel = addLinksHATEOASCrud(usuarioPag);
        addLinksPaginacao(usuarioPag, pagedModel, page, size);
        return ResponseEntity.ok(pagedModel);
    }


    @GetMapping
    @PreAuthorize("hasAnyAuthority('GRP_SIPE_ADMIN', 'GRP_SIPE_RH', 'GRP_SIPE_DIRETOR')")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioResponse>>> listarUsuarios(@RequestParam(required = false) String nome,
                                                                                        @RequestParam(required = false) String cracha,
                                                                                        @RequestParam(required = false) String matricula,
                                                                                        @RequestParam(required = false, name = "id_lotacao", defaultValue = "15") Integer idLotacao) {
        List<? extends Usuario> usuarioList;
        if (nome == null && cracha == null && matricula == null) {
            log.info("Lista listaUsuarios All");
            usuarioList = servidorService.listar(idLotacao);
        } else {
            var isNumerico = cracha != null && cracha.matches("\\d{3,16}");
            log.info("Lista listaUsuarios filtered");
            usuarioList = servidorService.listar(nome, isNumerico ? Integer.parseInt(cracha) : 0, matricula, idLotacao);
        }

        if (usuarioList.isEmpty()) {
            return ResponseEntity.ok(CollectionModel.empty());
        }

        var models = usuarioList.stream().map(usuario ->
            EntityModel.of(usuarioMapper.toResponse(usuario),
                linkTo(methodOn(UsuarioReadController.class).buscaUsuario(usuario.getMatricula())).withSelfRel(),
                linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(usuario.getId())).withRel("delete")
            )
        ).toList();

        return ResponseEntity.ok(CollectionModel.of(models));
    }

    private void addLinksPaginacao(Page<? extends Usuario> usuarioPag, PagedModel<EntityModel<UsuarioResponse>> pagedModel, int page, int size) {
        if (usuarioPag.hasPrevious()) {
            pagedModel.add(Link.of(linkTo(methodOn(UsuarioReadController.class).paginarUsuarios(null, null, null, page - 1, size)).toString(), "prev"));
        }
        if (usuarioPag.hasNext()) {
            pagedModel.add(Link.of(linkTo(methodOn(UsuarioReadController.class).paginarUsuarios(null, null, null, page + 1, size)).toString(), "next"));
        }
    }

    private PagedModel<EntityModel<UsuarioResponse>> addLinksHATEOASCrud(Page<? extends Usuario> usuarioPage) {
        return PagedModel.of(
                usuarioPage.getContent().stream()
                        .map(usuario -> EntityModel.of(usuarioMapper.toResponse(usuario),
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