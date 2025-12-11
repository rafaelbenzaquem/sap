package br.jus.trf1.sipe.usuario.application.web;

import br.jus.trf1.sipe.usuario.application.web.dto.UsuarioResponse;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.domain.port.in.UsuarioServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping(value = "/{matricula}")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<EntityModel<UsuarioResponse>> buscaUsuario(@PathVariable("matricula") String matricula) {
        var usuario = usuarioService.buscaPorMatricula(matricula);
        return ResponseEntity.ok(EntityModel.of(UsuarioWebMapper.toResponse(usuario),
                linkTo(methodOn(UsuarioReadController.class).buscaUsuario(matricula)).withSelfRel(),
                linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(usuario.getId())).withRel("delete")
        ));
    }

    @GetMapping("/pag")
    @PreAuthorize("hasAnyAuthority('GRP_SIPE_ADMIN', 'GRP_SIPE_RH', 'GRP_SIPE_DIRETOR')")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioResponse>>> paginarUsuarios(@RequestParam(required = false) String nome,
                                                                                         @RequestParam(required = false) String cracha,
                                                                                         @RequestParam(required = false) String matricula,
                                                                                         @RequestParam(defaultValue = "0") int page,
                                                                                         @RequestParam(defaultValue = "5") int size) {
        List<? extends Usuario> usuarios;
        long totalElements = 90;
        if (nome == null && cracha == null && matricula == null) {
            log.info("Pagina listaUsuarios All");
            usuarios = usuarioService.pagina(page, size);
            totalElements = usuarioService.conta();
        } else {
            var isNumerico = cracha != null && cracha.matches("\\d{3,16}");
            log.info("Pagina listaUsuarios filtered");
            usuarios = usuarioService.paginaPorNomeOuCrachaOuMatricula(nome, isNumerico ? Integer.parseInt(cracha) : 0, matricula, page, size);
            totalElements = usuarioService.contaPorNomeOuCrachaOuMatricula(nome, isNumerico ? Integer.parseInt(cracha) : 0, matricula);
        }

        var pagedModel = addLinksHATEOASCrud(usuarios, page, size, totalElements);
        addLinksPaginacao(usuarios, pagedModel, page, size, totalElements);
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
            log.info("Pagina listaUsuarios All");
            usuarioList = usuarioService.lista();
        } else {
            var isNumerico = cracha != null && cracha.matches("\\d{3,16}");
            log.info("Pagina listaUsuarios filtered");
            usuarioList = usuarioService.listaPorNomeOuCrachaOuMatricula(nome, isNumerico ? Integer.parseInt(cracha) : 0, matricula);
        }

        var models = usuarioList.stream().map(usuario ->
                EntityModel.of(UsuarioWebMapper.toResponse(usuario),
                        linkTo(methodOn(UsuarioReadController.class).buscaUsuario(usuario.getMatricula())).withSelfRel(),
                        linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(usuario.getId())).withRel("delete")
                )
        ).toList();

        return ResponseEntity.ok(CollectionModel.of(models));
    }

    private void addLinksPaginacao(List<? extends Usuario> usuarios, PagedModel<EntityModel<UsuarioResponse>> pagedModel, int page, int size, long totalElements) {
        var totalDePaginas = (int) Math.ceil(((double) totalElements / (double) size));

        if (page > 0) {
            pagedModel.add(Link.of(linkTo(methodOn(UsuarioReadController.class).paginarUsuarios(null, null, null, page - 1, size)).toString(), "prev"));
        }
        if (page < totalDePaginas) {
            pagedModel.add(Link.of(linkTo(methodOn(UsuarioReadController.class).paginarUsuarios(null, null, null, page + 1, size)).toString(), "next"));
        }
    }

    private PagedModel<EntityModel<UsuarioResponse>> addLinksHATEOASCrud(List<? extends Usuario> usuarios, int page, int size, long totalElements) {
        return PagedModel.of(
                usuarios.stream()
                        .map(usuario -> EntityModel.of(UsuarioWebMapper.toResponse(usuario),
                                linkTo(methodOn(UsuarioReadController.class).buscaUsuario(usuario.getMatricula())).withSelfRel(),
                                linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(usuario.getId())).withRel("delete")
                        )).toList(),
                new PagedModel.PageMetadata(
                        size,
                        page,
                        totalElements,
                        (long) Math.ceil(((double) totalElements / (double) size)))
        );
    }
}