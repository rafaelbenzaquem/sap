package br.jus.trf1.sipe.relatorio.web;

import br.jus.trf1.sipe.relatorio.RelatorioLotacaoService;
import br.jus.trf1.sipe.relatorio.RelatorioService;
import br.jus.trf1.sipe.relatorio.RelatorioUsuarioService;
import br.jus.trf1.sipe.servidor.infrastructure.persistence.ServidorJpa;
import br.jus.trf1.sipe.servidor.ServidorService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;

@Slf4j
@RestController
@RequestMapping("/v1/sipe/relatorios")
public class RelatorioController {

    private final RelatorioUsuarioService relatorioUsuarioService;
    private final RelatorioLotacaoService relatorioLotacaoService;
    private final ServidorService servidorService;

    public RelatorioController(RelatorioUsuarioService relatorioUsuarioService,
                               RelatorioLotacaoService relatorioLotacaoService,
                               ServidorService servidorService) {
        this.relatorioUsuarioService = relatorioUsuarioService;
        this.relatorioLotacaoService = relatorioLotacaoService;
        this.servidorService = servidorService;
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


    @GetMapping("/lotacao/usuario/{matricula}")
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
    public ResponseEntity<Resource> downloadRelatorioLotacaoPorUsuario(@PathVariable("matricula") String matricula,
                                                             @RequestParam("inicio")
                                                             @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                             LocalDate inicio,
                                                             @RequestParam("fim")
                                                             @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                             LocalDate fim) throws JRException {
        return templateRelatorio(matricula,inicio, fim, relatorioLotacaoService);
    }


    @GetMapping("/lotacao/{idLotacao}")
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
    public ResponseEntity<Resource> downloadRelatorioLotacao(@PathVariable("idLotacao") Integer idLotacao,
                                                             @RequestParam("inicio")
                                                             @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                             LocalDate inicio,
                                                             @RequestParam("fim")
                                                             @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                             LocalDate fim) throws JRException {
        ServidorJpa servidor = servidorService.buscaDiretorLotacao(idLotacao);
        return templateRelatorio(servidor.getMatricula(),inicio, fim, relatorioLotacaoService);
    }


    public ResponseEntity<Resource> templateRelatorio(String matricula,
                                                      LocalDate inicio,
                                                      LocalDate fim,
                                                      RelatorioService relatorioService) throws JRException {
        log.info("Iniciando processamento do relatorio ");
        log.info("Matricula: {}", matricula);
        log.info("Periodo: {} a {}", inicio, fim);

        if (inicio.isAfter(fim)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data inicial deve ser menor ou igual à final");
        }
        if (ChronoUnit.DAYS.between(inicio, fim) > 31) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Período máximo permitido é de 31 dias");
        }

        byte[] bytes = relatorioService.gerarRelatorio(matricula, inicio, fim);

        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(bytes));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"%s_%s_a_%s.pdf\"".formatted(matricula, inicio, fim));
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
