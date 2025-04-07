package br.jus.trf1.sap.externo.jsarh.ausencias.licenca;

import br.jus.trf1.sap.externo.jsarh.ausencias.licenca.dto.LicencaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static br.jus.trf1.sap.comum.util.ConstantesParaDataTempo.PADRAO_ENTRADA_DATA;

@Service
@FeignClient(url = "${servidor.jsarh.url}", fallback = LicencaServiceFallBackImpl.class, name = "licencas")
public interface LicencasService {

    @GetMapping(value = "/v1/sarh/servidores/{matricula}/ausencias/licencas", produces = "application/json")
    List<LicencaResponse> buscaLicencaServidorPorPeriodo(
            @PathVariable("matricula") String matricula,
            @RequestParam(name = "inicio", required = false) @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
            LocalDate inicio,
            @RequestParam(name = "fim", required = false) @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
            LocalDate fim);


    @GetMapping(value = "/v1/sarh/servidores/{matricula}/ausencias/licencas/{dia}", produces = "application/json")
    Optional<LicencaResponse> buscaLicencaServidorNoDia(
            @PathVariable("matricula")
            String matricula,
            @PathVariable("dia")
            @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
            LocalDate dia);

}
