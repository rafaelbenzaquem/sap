package br.jus.trf1.sap.vinculo;

import br.jus.trf1.sap.vinculo.dto.VinculoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sap/v1/vinculos")
public class ApagarVinculosController {

    private final VinculoRepository repository;

    public ApagarVinculosController(VinculoRepository repository) {
        this.repository = repository;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VinculoResponse> apagarVinculo(@PathVariable Integer id) {
        var vinculoOpt = repository.findById(id);
        if(vinculoOpt.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().body(vinculoOpt.get().toResponse());
        }
        return ResponseEntity.notFound().build();
    }
}
