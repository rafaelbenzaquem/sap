package br.jus.trf1.sap.vinculo.web;

import br.jus.trf1.sap.vinculo.Vinculo;
import br.jus.trf1.sap.vinculo.VinculoInexistenteException;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import br.jus.trf1.sap.vinculo.web.dto.VinculoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/sap/vinculos")
public class BuscaVinculosController {

    private final VinculoRepository repository;

    public BuscaVinculosController(VinculoRepository repository) {
        this.repository = repository;
    }

    @GetMapping()
    public ResponseEntity<List<VinculoResponse>> listarVinculos() {
        return ResponseEntity.ok(repository.findAll().stream().map(Vinculo::toResponse).toList());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VinculoResponse> lerVinculo(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(repository.findById(id).
                orElseThrow(() -> new VinculoInexistenteException("Vinculo id = %d não encontrado!".formatted(id))).
                toResponse());
    }
}
