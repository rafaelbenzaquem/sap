package br.jus.trf1.sap.vinculo.web;

import br.jus.trf1.sap.vinculo.Vinculo;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import br.jus.trf1.sap.vinculo.web.dto.NovoVinculoRequest;
import br.jus.trf1.sap.vinculo.web.dto.VinculoResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping(value = "/v1/sap/vinculos")
public class CriaVinculosController {

    private final VinculoRepository repository;

    public CriaVinculosController(VinculoRepository repository) {
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
