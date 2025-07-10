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

import java.time.LocalDate;
import java.time.YearMonth;

import static br.jus.trf1.sipe.comum.util.DataTempoUtil.paraString;
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
                                                                 @RequestParam("mes")
                                                                 Integer valorMes,
                                                                 @RequestParam("ano")
                                                                 Integer ano) {
        var mes = Mes.getMes(valorMes);
        log.info("Buscando Folha - {} - {} - {}", matricula, ano, mes.getNome());

        var folha = folhaService.buscarFolha(matricula, mes, ano).orElse(folhaService.abrirFolha(matricula, mes, ano));


        var diaInicial = LocalDate.of(ano, mes.getValor(), 1);
        var inicio = paraString(diaInicial);
        var diaFinal = YearMonth.of(ano, mes.getValor()).atEndOfMonth();
        var fim = paraString(diaFinal);


        var uriPonto = ServletUriComponentsBuilder.fromCurrentContextPath().
                path("/v1/sipe/pontos?matricula={matricula}&inicio={inicio}&fim={fim}").
                buildAndExpand(matricula, inicio, fim).toUriString();
        var pontoModel = EntityModel.of(FolhaResponse.of(folha),
                linkTo(methodOn(FolhaController.class).buscaFolha(matricula, valorMes, ano)).withSelfRel(),
                Link.of(uriPonto).withRel("pontos")
        );
        return ResponseEntity.ok(pontoModel);
    }


}
