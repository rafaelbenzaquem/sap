package br.jus.trf1.sipe.externo.jsarh.servidor;

import br.jus.trf1.sipe.externo.jsarh.servidor.dto.ServidorExternoResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ServidorExternoServiceFallBackImpl implements ServidorExternoService {
    @Override
    public Optional<ServidorExternoResponse> buscaDadosServidor(String matricula) {
        return Optional.empty();
    }
}
