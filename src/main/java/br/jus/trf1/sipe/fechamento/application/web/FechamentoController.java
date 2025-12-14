package br.jus.trf1.sipe.fechamento.application.web;

import br.jus.trf1.sipe.fechamento.domain.model.Fechamento;
import br.jus.trf1.sipe.fechamento.domain.port.in.FechamentoServicePort;
import br.jus.trf1.sipe.folha.application.web.FolhaController;
import br.jus.trf1.sipe.fechamento.application.web.dto.FechamentoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller para fechar folha de ponto e registrar saldos.
 */
@Slf4j
@RestController
@RequestMapping("/v1/sipe/folhas/fechamentos")
public class FechamentoController {

    private final FechamentoServicePort fechamentoServicePort;

    public FechamentoController(FechamentoServicePort fechamentoServicePort) {
        this.fechamentoServicePort = fechamentoServicePort;
    }

    /**
     * Fecha a folha de ponto para um servidor, calcula e persiste o saldo.
     * @param matricula identificador do servidor
     * @param mes valor numérico do mês (1-12)
     * @param ano ano da folha
     */
    @PostMapping
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
    public ResponseEntity<EntityModel<FechamentoResponse>> fecharFolha(
            @RequestParam String matricula,
            @RequestParam Integer mes,
            @RequestParam Integer ano) {
        log.info("Fechando folha - {} - {}/{}", matricula, mes, ano);
        Fechamento fechamento = fechamentoServicePort.fecharFolha(matricula, mes, ano);
        var resp = FechamentoResponse.of(fechamento);
        // Monta HATEOAS links
        Link self = linkTo(methodOn(FechamentoController.class)
                .fecharFolha(matricula, mes, ano)).withSelfRel();
        Link folhaLink = WebMvcLinkBuilder.linkTo(methodOn(FolhaController.class)
                .buscaFolha(matricula, mes, ano)).withRel("folha");
        return ResponseEntity.ok(EntityModel.of(resp, self, folhaLink));
    }
}