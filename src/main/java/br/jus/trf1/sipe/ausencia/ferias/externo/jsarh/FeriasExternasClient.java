package br.jus.trf1.sipe.ausencia.ferias.externo.jsarh;

import br.jus.trf1.sipe.ausencia.ferias.externo.jsarh.dto.FeriasExternasResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;

@Service
@FeignClient(url = "${jsarh.api.url}", fallback = FeriasExternasClientFallBackImpl.class, name = "ferias")
public interface FeriasExternasClient {

    @GetMapping(value = "/v1/sarh/servidores/{matricula}/ausencias/ferias", produces = "application/json")
    List<FeriasExternasResponse> buscaFeriasServidorPorPeriodo(
            @PathVariable("matricula") String matricula,
            @RequestParam(name = "inicio", required = false) @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
            LocalDate inicio,
            @RequestParam(name = "fim", required = false) @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
            LocalDate fim);

    @GetMapping(value = "/v1/sarh/servidores/{matricula}/ausencias/ferias/{dia}", produces = "application/json")
    Optional<FeriasExternasResponse> buscaFeriasServidorNoDia(
            @PathVariable("matricula") String matricula,
            @PathVariable("dia")
            @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
            LocalDate dia);


}

