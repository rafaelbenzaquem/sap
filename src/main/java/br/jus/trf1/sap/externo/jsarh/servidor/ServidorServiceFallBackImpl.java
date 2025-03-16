package br.jus.trf1.sap.externo.jsarh.servidor;

import br.jus.trf1.sap.externo.jsarh.servidor.dto.ServidorResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ServidorServiceFallBackImpl implements ServidorService{
    @Override
    public Optional<ServidorResponse> buscaDadosServidor(String matricula) {
        return Optional.empty();
    }
}
