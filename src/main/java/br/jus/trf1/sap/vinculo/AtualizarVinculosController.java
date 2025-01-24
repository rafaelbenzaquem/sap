package br.jus.trf1.sap.vinculo;

import br.jus.trf1.sap.vinculo.dto.VinculoAtualizadoRequest;
import br.jus.trf1.sap.vinculo.dto.VinculoResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/sap/v1//vinculos")
public class AtualizarVinculosController {

    private final VinculoRepository repository;

    public AtualizarVinculosController(VinculoRepository repository) {
        this.repository = repository;
    }

    @PutMapping
    public ResponseEntity<VinculoResponse> atualziarVinculo(@RequestBody @Valid VinculoAtualizadoRequest request) {

        var vinculoOpt = repository.findById(request.id());

        if(vinculoOpt.isPresent()) {
            var vinculo = vinculoOpt.get();
            vinculo.setNome(request.nome());
            vinculo.setCracha(request.cracha());
            vinculo.setMatricula(request.matricula());

            var vinculoSalvo = repository.save(vinculo);

            var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/vinculos/{id}").buildAndExpand(vinculoSalvo.getId()).toUri();

            return ResponseEntity.created(uriResponse).body(vinculoSalvo.toResponse());
        }
        return ResponseEntity.notFound().build();
    }


}
