package br.jus.trf1.sipe.relatorio.web;

import br.jus.trf1.sipe.relatorio.RelatorioLotacaoService;
import br.jus.trf1.sipe.relatorio.RelatorioService;
import br.jus.trf1.sipe.relatorio.RelatorioUsuarioService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;

@Slf4j
@RestController
@RequestMapping("/v1/sipe/relatorios")
public class RelatorioController {

    private final RelatorioUsuarioService relatorioUsuarioService;
    private final RelatorioLotacaoService relatorioLotacaoService;

    public RelatorioController(RelatorioUsuarioService relatorioUsuarioService,
                               RelatorioLotacaoService relatorioLotacaoService) {
        this.relatorioUsuarioService = relatorioUsuarioService;
        this.relatorioLotacaoService = relatorioLotacaoService;
    }

    @GetMapping("/{matricula}")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<Resource> downloadRelatorioPonto(@PathVariable("matricula") String matricula,
                                                           @RequestParam("inicio")
                                                           @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                           LocalDate inicio,
                                                           @RequestParam("fim")
                                                           @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                           LocalDate fim) throws JRException {
        return templateRelatorio(matricula,inicio,fim, relatorioUsuarioService);
    }


    @GetMapping("/lotacao/{matricula}")
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
    public ResponseEntity<Resource> downloadRelatorioLotacao(@PathVariable("matricula") String matricula,
                                                             @RequestParam("inicio")
                                                             @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                             LocalDate inicio,
                                                             @RequestParam("fim")
                                                             @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                             LocalDate fim) throws JRException {
        return templateRelatorio(matricula,inicio, fim, relatorioLotacaoService);
    }


    public ResponseEntity<Resource> templateRelatorio(String matricula,
                                                      LocalDate inicio,
                                                      LocalDate fim,
                                                      RelatorioService relatorioService) throws JRException {
        log.info("Iniciando processamento do relatorio ");
        log.info("Matricula: {}", matricula);
        log.info("Periodo: {} a {}", inicio, fim);


        byte[] bytes = relatorioService.gerarRelatorio(matricula, inicio, fim);

        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(bytes));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + matricula + ".pdf");
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
