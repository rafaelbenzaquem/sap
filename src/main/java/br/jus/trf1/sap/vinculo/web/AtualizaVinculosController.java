package br.jus.trf1.sap.vinculo.web;

import br.jus.trf1.sap.vinculo.exceptions.VinculoInexistenteException;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import br.jus.trf1.sap.vinculo.web.dto.VinculoAtualizadoRequest;
import br.jus.trf1.sap.vinculo.web.dto.VinculoResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/sap/vinculos")
public class AtualizaVinculosController {

    private final VinculoRepository repository;

    public AtualizaVinculosController(VinculoRepository repository) {
        this.repository = repository;
    }

    @PutMapping("/{id}")
    public ResponseEntity<VinculoResponse> atualziarVinculo(@PathVariable Integer id,
                                                            @RequestBody @Valid VinculoAtualizadoRequest request) {

        var vinculoOpt = repository.findById(id);

        if (vinculoOpt.isPresent()) {
            var vinculo = vinculoOpt.get();
            vinculo.setNome(request.nome());
            vinculo.setCracha(request.cracha());
            vinculo.setMatricula(request.matricula());

            var vinculoSalvo = repository.save(vinculo);

            return ResponseEntity.ok().body(vinculoSalvo.toResponse());
        }
        throw new VinculoInexistenteException(id);
    }


}
