package br.jus.trf1.sipe.registro.web;

import br.jus.trf1.sipe.registro.RegistroService;
import br.jus.trf1.sipe.registro.web.dto.RegistroResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/v1/sipe/registros")
public class RegistroDeleteController {

    private final RegistroService registroService;

    public RegistroDeleteController(RegistroService registroService) {
        this.registroService = registroService;
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<String> apagar(@PathVariable("id") Long id) {

        log.info("apagando registro {}", id);
        registroService.apagar(id);
        return ResponseEntity.ok("Registro id :" + id + " apagado com sucesso!");
    }

}
