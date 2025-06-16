package br.jus.trf1.sipe.folha.web;

import br.jus.trf1.sipe.folha.FolhaService;
import br.jus.trf1.sipe.folha.Mes;
import br.jus.trf1.sipe.folha.web.dto.FolhaResponse;
import br.jus.trf1.sipe.ponto.web.PontoReadController;
import br.jus.trf1.sipe.ponto.web.dto.PontoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/v1/sipe/folhas")
public class FolhaController {

    private final FolhaService folhaService;

    public FolhaController(FolhaService folhaService) {
        this.folhaService = folhaService;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<EntityModel<FolhaResponse>> buscaFolha(@RequestParam("matricula")
                                                                 String matricula,
                                                                 @RequestParam("ano")
                                                                 Integer ano,
                                                                 @RequestParam("mes")
                                                                 Integer valorMes) {
        var mes = Mes.getMes(valorMes);
        log.info("Buscando Folha - {} - {} - {}", matricula, ano, mes.getNome());

        var folha = folhaService.buscarFolha(matricula, mes, ano).orElse(folhaService.abrirFolha(matricula, mes, ano));


        var uri = ServletUriComponentsBuilder.fromCurrentContextPath().
                path("/v1/sipe/folhas?matricula={matricula}&ano={ano}&mes={mes}").
                buildAndExpand(matricula, ano, mes).toUriString();
        var pontoModel = EntityModel.of(FolhaResponse.of(folha),
                linkTo(methodOn(FolhaController.class).buscaFolha(matricula, ano, valorMes)).withSelfRel(),
                Link.of(uri).withRel("pontos")
        );
        return ResponseEntity.ok(pontoModel);
    }


}
