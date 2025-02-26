package br.jus.trf1.sap.externo.jsarh.ausencias.ferias;

import br.jus.trf1.sap.externo.jsarh.ausencias.ferias.dto.FeriasResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@FeignClient(url = "${servidor.jsarh.url}", name = "ferias")
public interface FeriasService {

    @GetMapping(value = "/v1/sarh/servidores/{matricula}/ausencias/ferias", produces = "application/json")
    List<FeriasResponse> buscaFerias(
            @PathVariable("matricula") String matricula,
            @RequestParam(name = "inicio", required = false) String inicio,
            @RequestParam(name = "fim", required = false) String fim);

}

