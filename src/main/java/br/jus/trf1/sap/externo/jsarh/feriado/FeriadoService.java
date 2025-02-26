package br.jus.trf1.sap.externo.jsarh.feriado;

import br.jus.trf1.sap.externo.jsarh.feriado.dto.FeriadoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@FeignClient(url = "${servidor.jsarh.url}", name = "feriado")
public interface FeriadoService {


    @GetMapping(value = "/v1/sarh/feriados", produces = "application/json")
    List<FeriadoResponse> buscaFeriados(@RequestParam(name = "inicio", required = false) String inicio,
                                        @RequestParam(name = "fim", required = false) String fim,
                                        @RequestParam(name = "ano", required = false) Integer ano);


}
