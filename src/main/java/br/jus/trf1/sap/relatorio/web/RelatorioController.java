package br.jus.trf1.sap.relatorio.web;

import br.jus.trf1.sap.relatorio.RelatorioService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

import static br.jus.trf1.sap.util.DateTimeUtils.criaLocalDate;

@RestController
@RequestMapping("/v1/sap/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<Resource> downloadRelatorio(@PathVariable("matricula") Integer matricula,
                                                      @RequestParam("inicio") String inicio,
                                                      @RequestParam("fim") String fim) throws JRException{

        byte[] bytes = relatorioService.gerarRelatorio(matricula, criaLocalDate(inicio), criaLocalDate(fim));

        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(bytes));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+matricula+".pdf");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
