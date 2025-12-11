package br.jus.trf1.sipe.servidor.application.jsarh;

import br.jus.trf1.sipe.servidor.application.jsarh.dto.ServidorJSarhResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
@FeignClient(url = "${jsarh.api.url}", name = "servidor", fallback = ServidorJSarhClientFallBackImpl.class)
public interface ServidorJSarhClient {

    @GetMapping(value = "/v1/sarh/servidores/{matricula}", produces = "application/json")
    Optional<ServidorJSarhResponse> buscaDadosServidor(@PathVariable("matricula") String matricula);
}
