package br.jus.trf1.sipe.externo.jsarh.feriado;

import br.jus.trf1.sipe.externo.jsarh.feriado.dto.FeriadoExternalResponse;
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
@FeignClient(url = "${jsarh.api.url}",fallback = FeriadoExternalClientFallBackImpl.class, name = "feriado")
public interface FeriadoExternalClient {


    @GetMapping(value = "/v1/sarh/feriados", produces = "application/json")
    List<FeriadoExternalResponse> buscaFeriados(@RequestParam(name = "inicio", required = false) @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                        LocalDate inicio,
                                                @RequestParam(name = "fim", required = false) @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                        LocalDate fim,
                                                @RequestParam(name = "ano", required = false) Integer ano);

    @GetMapping(value = "/v1/sarh/feriados/{dia}", produces = "application/json")
    Optional<FeriadoExternalResponse> buscaFeriadoDoDia(@PathVariable("dia")
                                                @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                LocalDate dia);


}
