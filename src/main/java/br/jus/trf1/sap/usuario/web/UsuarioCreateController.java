package br.jus.trf1.sap.usuario.web;

import br.jus.trf1.sap.usuario.UsuarioRepository;
import br.jus.trf1.sap.usuario.web.dto.UsuarioNovoRequest;
import br.jus.trf1.sap.usuario.web.dto.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping(value = "/v1/sap/usuarios")
public class UsuarioCreateController {

    private final UsuarioRepository repository;

    public UsuarioCreateController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @PostMapping()
    public ResponseEntity<UsuarioResponse> criarVinculo(@RequestBody @Valid UsuarioNovoRequest request) {
        log.info("Criando vinculo: {}", request);
        var vinculoSalvo = repository.save(request.paraEntidade());

        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/vinculos/{id}").buildAndExpand(vinculoSalvo.getId()).toUri();

        return ResponseEntity.created(uriResponse).body(vinculoSalvo.toResponse());
    }

}
