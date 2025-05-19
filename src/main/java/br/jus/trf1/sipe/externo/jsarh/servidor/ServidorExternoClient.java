package br.jus.trf1.sipe.externo.jsarh.servidor;

import br.jus.trf1.sipe.externo.jsarh.servidor.dto.ServidorExternoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
@FeignClient(url = "${servidor.jsarh.url}", name = "servidor", fallback = ServidorExternoClientFallBackImpl.class)
public interface ServidorExternoClient {

    @GetMapping(value = "/v1/sarh/servidores/{matricula}", produces = "application/json")
    Optional<ServidorExternoResponse> buscaDadosServidor(@PathVariable("matricula") String matricula);
}
