package br.jus.trf1.sap.externo.jsarh.feriado;

import br.jus.trf1.sap.externo.jsarh.feriado.dto.FeriadoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

import static br.jus.trf1.sap.util.ConstantesDataTempoUtil.PADRAO_ENTRADA_DATA;

@Service
@FeignClient(url = "${servidor.jsarh.url}", name = "feriado")
public interface FeriadoService {


    @GetMapping(value = "/v1/sarh/feriados", produces = "application/json")
    List<FeriadoResponse> buscaFeriados(@RequestParam(name = "inicio", required = false) @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                        LocalDate inicio,
                                        @RequestParam(name = "fim", required = false) @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                        LocalDate fim,
                                        @RequestParam(name = "ano", required = false) Integer ano);


}
