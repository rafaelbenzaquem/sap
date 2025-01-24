package br.jus.trf1.sap.vinculo;

import br.jus.trf1.sap.vinculo.dto.VinculoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/sap/v1/vinculos")
public class LerVinculosController {

    private final VinculoRepository repository;

    public LerVinculosController(VinculoRepository repository) {
        this.repository = repository;
    }

    @GetMapping()
    public ResponseEntity<List<VinculoResponse>> listarVinculos() {
        return ResponseEntity.ok(repository.findAll().stream().map(Vinculo::toResponse).toList());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VinculoResponse> lerVinculo(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(repository.findById(id).orElseThrow().toResponse());
    }


//    @GetMapping
//    public ResponseEntity<VinculoResponse> buscarVinculoPorNome(@RequestParam(name = "nome", required = false) String nome) {
//
//        return ResponseEntity.ok(repository.findVinculoByNome(nome).orElseThrow().toResponse());
//    }
//
//    @GetMapping
//    public ResponseEntity<VinculoResponse> buscarVinculoPorMatricula(@RequestParam(name = "matricula", required = false) Integer matricula) {
//
//        return ResponseEntity.ok(repository.findVinculoByMatricula(matricula).orElseThrow().toResponse());
//    }
//
//    @GetMapping
//    public ResponseEntity<VinculoResponse> buscarVinculoPorCracha(@RequestParam(name = "cracha", required = false) String cracha) {
//
//        return ResponseEntity.ok(repository.findVinculoByCracha(cracha).orElseThrow().toResponse());
//    }

}
