package br.jus.trf1.sipe.usuario.web;

import br.jus.trf1.sipe.usuario.exceptions.UsuarioInexistenteException;
import br.jus.trf1.sipe.usuario.UsuarioRepository;
import br.jus.trf1.sipe.usuario.web.dto.UsuarioAtualizadoRequest;
import br.jus.trf1.sipe.usuario.web.dto.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/sipe/usuarios")
public class UsuarioUpdateController {

    private final UsuarioRepository repository;

    public UsuarioUpdateController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioResponse>> atualziarVinculo(@PathVariable Integer id,
                                                                         @RequestBody
                                                                         @Valid UsuarioAtualizadoRequest request) {

        var usuarioOpt = repository.findById(id);

        if (usuarioOpt.isPresent()) {
            var usuario = usuarioOpt.get();
            usuario.setNome(request.nome());
            usuario.setCracha(request.cracha());
            usuario.setMatricula(request.matricula());
            usuario.setHoraDiaria(request.horaDiaria());

            var usuarioAtualizado = repository.save(usuario);

            var entityModel = EntityModel.of(
                    usuarioAtualizado.toResponse(),
                    linkTo(methodOn(UsuarioReadController.class).buscaVinculo(usuarioAtualizado.getId())).withSelfRel(),
                    linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(usuarioAtualizado.getId())).withRel("delete"));

            return ResponseEntity.ok().body(entityModel);
        }
        throw new UsuarioInexistenteException(id);
    }


}
