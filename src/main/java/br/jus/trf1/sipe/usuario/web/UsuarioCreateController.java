package br.jus.trf1.sipe.usuario.web;

import br.jus.trf1.sipe.usuario.UsuarioRepository;
import br.jus.trf1.sipe.usuario.web.dto.UsuarioNovoRequest;
import br.jus.trf1.sipe.usuario.web.dto.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/v1/sipe/usuarios")
public class UsuarioCreateController {

    private final UsuarioRepository repository;

    public UsuarioCreateController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @PostMapping()
    public ResponseEntity<EntityModel<UsuarioResponse>> cadastraUsuario(@RequestBody @Valid UsuarioNovoRequest request) {
        log.info("Criando usuario: {}", request);
        var usuario = repository.save(request.paraEntidade());

        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/vinculos/{id}").buildAndExpand(usuario.getId()).toUri();

        var entityModel = EntityModel.of(usuario.toResponse(), linkTo(methodOn(UsuarioReadController.class).buscaVinculo(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(usuario.getId())).withRel("delete"));

        return ResponseEntity.created(uriResponse).body(entityModel);
    }

}
