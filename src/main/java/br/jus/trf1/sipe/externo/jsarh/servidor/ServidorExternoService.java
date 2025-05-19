package br.jus.trf1.sipe.externo.jsarh.servidor;

import br.jus.trf1.sipe.externo.jsarh.servidor.dto.ServidorExternoResponse;
import br.jus.trf1.sipe.externo.jsarh.servidor.exceptions.ServidorExternoInexistenteException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServidorExternoService {

    private final ServidorExternoClient servidorExternoClient;

    public ServidorExternoService(ServidorExternoClient servidorExternoClient) {
        this.servidorExternoClient = servidorExternoClient;
    }

    public ServidorExternoResponse  buscaServidorExterno(String matricula) {
        Optional<ServidorExternoResponse> opt = servidorExternoClient.buscaDadosServidor(matricula);
        return opt.orElseThrow(()-> new ServidorExternoInexistenteException("Servidor '%s' inexistente".formatted(matricula)));
    }

}
