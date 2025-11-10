package br.jus.trf1.sipe.servidor.externo.jsarh;

import br.jus.trf1.sipe.servidor.externo.jsarh.dto.ServidorExternoResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ServidorExternoClientFallBackImpl implements ServidorExternoClient {
    @Override
    public Optional<ServidorExternoResponse> buscaDadosServidor(String matricula) {
        return Optional.empty();
    }
}
