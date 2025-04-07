package br.jus.trf1.sap.usuario.web;

import br.jus.trf1.sap.usuario.exceptions.UsuarioInexistenteException;
import br.jus.trf1.sap.usuario.UsuarioRepository;
import br.jus.trf1.sap.usuario.web.dto.UsuarioAtualizadoRequest;
import br.jus.trf1.sap.usuario.web.dto.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/sap/usuarios")
public class UsuarioUpdateController {

    private final UsuarioRepository repository;

    public UsuarioUpdateController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualziarVinculo(@PathVariable Integer id,
                                                            @RequestBody @Valid UsuarioAtualizadoRequest request) {

        var vinculoOpt = repository.findById(id);

        if (vinculoOpt.isPresent()) {
            var vinculo = vinculoOpt.get();
            vinculo.setNome(request.nome());
            vinculo.setCracha(request.cracha());
            vinculo.setMatricula(request.matricula());

            var vinculoSalvo = repository.save(vinculo);

            return ResponseEntity.ok().body(vinculoSalvo.toResponse());
        }
        throw new UsuarioInexistenteException(id);
    }


}
