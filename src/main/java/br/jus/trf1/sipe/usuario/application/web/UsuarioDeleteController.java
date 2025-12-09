package br.jus.trf1.sipe.usuario.application.web;

import br.jus.trf1.sipe.usuario.UsuarioMapper;
import br.jus.trf1.sipe.usuario.application.web.dto.UsuarioResponse;
import br.jus.trf1.sipe.usuario.domain.port.in.UsuarioServicePort;
import br.jus.trf1.sipe.usuario.domain.port.out.UsuarioRepositoryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/sipe/usuarios")
public class UsuarioDeleteController {

    private final UsuarioServicePort usuarioRepositoryPort;

    public UsuarioDeleteController(UsuarioServicePort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
    public ResponseEntity<UsuarioResponse> apagaVinculo(@PathVariable Integer id) {
            var usuario = usuarioRepositoryPort.apagaPorId(id);
            return ResponseEntity.ok().body(UsuarioMapper.toResponse(usuario));
    }
}
