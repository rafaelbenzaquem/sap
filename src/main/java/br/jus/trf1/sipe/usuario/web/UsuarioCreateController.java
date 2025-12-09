package br.jus.trf1.sipe.usuario.web;

import br.jus.trf1.sipe.servidor.ServidorService;
import br.jus.trf1.sipe.usuario.UsuarioService;
import br.jus.trf1.sipe.usuario.web.dto.UsuarioNovoRequest;
import br.jus.trf1.sipe.usuario.web.dto.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/v1/sipe/usuarios")
public class UsuarioCreateController {


    private final UsuarioService usuarioService;
    private final ServidorService servidorService;

    public UsuarioCreateController(UsuarioService usuarioService, ServidorService servidorService) {
        this.usuarioService = usuarioService;
        this.servidorService = servidorService;
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('GRP_SIPE_ADMIN', 'GRP_SIPE_RH')")
    public ResponseEntity<EntityModel<UsuarioResponse>> cadastraUsuario(@RequestBody @Valid UsuarioNovoRequest request) {
        log.info("Criando usuarioJPA: {}", request);


        var usuario = usuarioService.salve(request.paraEntidade());

        servidorService.atualizaDadosNoSarh(usuario.getMatricula());

        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/usuarios/{matricula}").buildAndExpand(usuario.getMatricula()).toUri();

        var entityModel = EntityModel.of(usuario.toResponse(), linkTo(methodOn(UsuarioReadController.class).buscaUsuario(usuario.getMatricula())).withSelfRel(),
                linkTo(methodOn(UsuarioDeleteController.class).apagaVinculo(usuario.getId())).withRel("delete"));

        return ResponseEntity.created(uriResponse).body(entityModel);
    }

}
