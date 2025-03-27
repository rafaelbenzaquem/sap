package br.jus.trf1.sap.vinculo.web;

import br.jus.trf1.sap.vinculo.exceptions.VinculoInexistenteException;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import br.jus.trf1.sap.vinculo.web.dto.VinculoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/sap/vinculos")
public class ApagaVinculosController {

    private final VinculoRepository repository;

    public ApagaVinculosController(VinculoRepository repository) {
        this.repository = repository;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VinculoResponse> apagaVinculo(@PathVariable Integer id) {
        var vinculoOpt = repository.findById(id);
        if (vinculoOpt.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().body(vinculoOpt.get().toResponse());
        }
        throw new VinculoInexistenteException(id);
    }
}
