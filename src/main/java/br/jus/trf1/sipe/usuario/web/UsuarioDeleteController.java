package br.jus.trf1.sipe.usuario.web;

import br.jus.trf1.sipe.usuario.exceptions.UsuarioInexistenteException;
import br.jus.trf1.sipe.usuario.UsuarioRepository;
import br.jus.trf1.sipe.usuario.web.dto.UsuarioResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/sipe/usuarios")
public class UsuarioDeleteController {

    private final UsuarioRepository repository;

    public UsuarioDeleteController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
    public ResponseEntity<UsuarioResponse> apagaVinculo(@PathVariable Integer id) {
        var vinculoOpt = repository.findById(id);
        if (vinculoOpt.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().body(vinculoOpt.get().toResponse());
        }
        throw new UsuarioInexistenteException(id);
    }
}
