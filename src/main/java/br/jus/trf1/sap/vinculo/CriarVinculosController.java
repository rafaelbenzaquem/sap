package br.jus.trf1.sap.vinculo;

import br.jus.trf1.sap.vinculo.dto.NovoVinculoRequest;
import br.jus.trf1.sap.vinculo.dto.VinculoResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/vinculos")
public class CriarVinculosController {

    private final VinculoRepository repository;

    public CriarVinculosController(VinculoRepository repository) {
        this.repository = repository;
    }

    @PostMapping()
    public ResponseEntity<VinculoResponse> criarVinculo(@RequestBody @Valid NovoVinculoRequest request) {
        log.info("Criando vinculo: {}", request);
        var vinculoSalvo = repository.save(Vinculo.of(request));

        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/vinculos/{id}").buildAndExpand(vinculoSalvo.getId()).toUri();

        return ResponseEntity.created(uriResponse).body(vinculoSalvo.toResponse());
    }


}
