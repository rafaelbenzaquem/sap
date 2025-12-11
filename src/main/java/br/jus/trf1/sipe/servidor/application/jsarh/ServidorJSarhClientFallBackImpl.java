package br.jus.trf1.sipe.servidor.application.jsarh;

import br.jus.trf1.sipe.servidor.application.jsarh.dto.ServidorJSarhResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ServidorJSarhClientFallBackImpl implements ServidorJSarhClient {
    @Override
    public Optional<ServidorJSarhResponse> buscaDadosServidor(String matricula) {
        return Optional.empty();
    }
}
