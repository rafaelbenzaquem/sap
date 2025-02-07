package br.jus.trf1.sap.relatorio.web;

import br.jus.trf1.sap.relatorio.RelatorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.jus.trf1.sap.util.DateTimeUtils.criaLocalDate;

@RestController
@RequestMapping("/v1/sap/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/{matricula}")
    public ResponseEntity downloadRelatorio(@PathVariable("matricula") Integer matricula,
                                            @RequestParam("inicio") String inicio,
                                            @RequestParam("fim") String fim) {

        relatorioService.gerarRelatorio(matricula, criaLocalDate(inicio), criaLocalDate(fim));


        return ResponseEntity.ok().build();
    }

}
