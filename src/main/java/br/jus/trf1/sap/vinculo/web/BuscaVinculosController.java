package br.jus.trf1.sap.vinculo.web;

import br.jus.trf1.sap.vinculo.Vinculo;
import br.jus.trf1.sap.vinculo.exceptions.VinculoInexistenteException;
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


    @GetMapping
    public ResponseEntity<List<VinculoResponse>> listarVinculosPorNomeOuCrachaOuMatricula(@RequestParam(required = false)
                                                                                          String nome,
                                                                                          @RequestParam(required = false)
                                                                                          String cracha,
                                                                                          @RequestParam(required = false)
                                                                                          String matricula) {
        if (nome == null && cracha == null && matricula == null)
            return ResponseEntity.ok(repository.findAll().stream().map(Vinculo::toResponse).toList());

        return ResponseEntity.ok(repository.buscarVinculosPorNomeOuCrachaOuMatricula(nome, cracha, matricula).stream().
                map(Vinculo::toResponse).
                toList());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VinculoResponse> buscaVinculo(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(repository.findById(id).
                orElseThrow(() -> new VinculoInexistenteException(id)).
                toResponse());
    }
}
