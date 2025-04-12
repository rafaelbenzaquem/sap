package br.jus.trf1.sipe.externo.jsarh.ausencias.ferias;

import br.jus.trf1.sipe.externo.jsarh.ausencias.ferias.dto.FeriasResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static br.jus.trf1.sipe.comum.util.ConstantesParaDataTempo.PADRAO_ENTRADA_DATA;

@Service
@FeignClient(url = "${servidor.jsarh.url}", fallback = FeriasServiceFallBackImpl.class, name = "ferias")
public interface FeriasService {

    @GetMapping(value = "/v1/sarh/servidores/{matricula}/ausencias/ferias", produces = "application/json")
    List<FeriasResponse> buscaFeriasServidorPorPeriodo(
            @PathVariable("matricula") String matricula,
            @RequestParam(name = "inicio", required = false) @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
            LocalDate inicio,
            @RequestParam(name = "fim", required = false) @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
            LocalDate fim);

    @GetMapping(value = "/v1/sarh/servidores/{matricula}/ausencias/ferias/{dia}", produces = "application/json")
    Optional<FeriasResponse> buscaFeriasServidorNoDia(
            @PathVariable("matricula") String matricula,
            @PathVariable("dia")
            @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
            LocalDate dia);


}

