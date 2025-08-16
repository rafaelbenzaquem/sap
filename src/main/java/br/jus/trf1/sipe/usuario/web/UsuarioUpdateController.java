package br.jus.trf1.sipe.usuario.web;

import br.jus.trf1.sipe.usuario.UsuarioService;
import br.jus.trf1.sipe.usuario.web.dto.UsuarioAtualizadoRequest;
import br.jus.trf1.sipe.usuario.web.dto.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/sipe/usuarios")
public class UsuarioUpdateController {

    private final UsuarioService service;

    public UsuarioUpdateController(UsuarioService service) {
        this.service = service;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
    public ResponseEntity<EntityModel<UsuarioResponse>> atualziarVinculo(@PathVariable Integer id,
                                                                         @RequestBody
                                                                         @Valid UsuarioAtualizadoRequest request) {

        var usuario = service.buscaPorId(id);
        usuario.setNome(request.nome());
        usuario.setCracha(Integer.parseInt(request.cracha()));
        usuario.setMatricula(request.matricula());
        usuario.setHoraDiaria(request.horaDiaria());

        var usuarioAtualizado = service.salve(usuario);

        var entityModel = EntityModel.of(
                usuarioAtualizado.toResponse(),
                linkTo(methodOn(UsuarioReadController.class).buscaUsuario(usuarioAtualizado.getMatricula())).withSelfRel(),
                linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(usuarioAtualizado.getId())).withRel("delete"));

        return ResponseEntity.ok().body(entityModel);
    }


}
