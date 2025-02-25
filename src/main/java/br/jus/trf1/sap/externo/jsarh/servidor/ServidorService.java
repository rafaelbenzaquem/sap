package br.jus.trf1.sap.externo.jsarh.servidor;

import br.jus.trf1.sap.externo.jsarh.servidor.dto.ServidorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(url = "${servidor.jsarh.url}", name = "servidor")
public interface ServidorService {

    @GetMapping(value = "/v1/sarh/servidores/{matricula}", produces = "application/json")
    ServidorResponse buscaDadosServidor(@PathVariable("matricula") String matricula);
}
