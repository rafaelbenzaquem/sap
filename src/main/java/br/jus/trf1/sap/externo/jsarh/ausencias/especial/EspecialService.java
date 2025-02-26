package br.jus.trf1.sap.externo.jsarh.ausencias.especial;

import br.jus.trf1.sap.externo.jsarh.ausencias.especial.dto.EspecialResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@FeignClient(url = "${servidor.jsarh.url}", name = "especiais")
public interface EspecialService {

    @GetMapping(value = "/v1/sarh/servidores/{matricula}/ausencias/especiais", produces = "application/json")
    List<EspecialResponse> buscaAusenciasEspeciais(
            @PathVariable("matricula") String matricula,
            @RequestParam(name = "inicio", required = false) String inicio,
            @RequestParam(name = "fim", required = false) String fim);

}
