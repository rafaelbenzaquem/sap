package br.jus.trf1.sipe.registro.web;

import br.jus.trf1.sipe.registro.RegistroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> apagar(@PathVariable("id") Long idRegistro, @RequestParam("id_pedido_alteracao") Long idPedidoAlteracao) {

        log.info("Apagando registro {}", idRegistro);
        registroService.apagar(idRegistro, idPedidoAlteracao);
        return ResponseEntity.ok("Registro id :" + idRegistro + " apagado com sucesso!");
    }

}
