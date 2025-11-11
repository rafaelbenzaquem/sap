package br.jus.trf1.sipe.feriado.externo.jsarh;

import br.jus.trf1.sipe.feriado.externo.jsarh.dto.FeriadoJSarhResponse;
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
@FeignClient(url = "${jsarh.api.url}",fallback = FeriadoJSarhClientFallBackImpl.class, name = "feriado")
public interface FeriadoJSarhClient {


    @GetMapping(value = "/v1/sarh/feriados", produces = "application/json")
    List<FeriadoJSarhResponse> buscaFeriados(@RequestParam(name = "inicio", required = false) @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                        LocalDate inicio,
                                             @RequestParam(name = "fim", required = false) @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                        LocalDate fim,
                                             @RequestParam(name = "ano", required = false) Integer ano);

    @GetMapping(value = "/v1/sarh/feriados/{dia}", produces = "application/json")
    Optional<FeriadoJSarhResponse> buscaFeriadoDoDia(@PathVariable("dia")
                                                @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                LocalDate dia);


}
