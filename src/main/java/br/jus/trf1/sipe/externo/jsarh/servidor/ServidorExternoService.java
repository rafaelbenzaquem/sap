package br.jus.trf1.sipe.externo.jsarh.servidor;

import br.jus.trf1.sipe.externo.jsarh.servidor.dto.ServidorExternoResponse;
import br.jus.trf1.sipe.externo.jsarh.servidor.exceptions.ServidorExternoInexistenteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ServidorExternoService {

    private final ServidorExternoClient servidorExternoClient;

    public ServidorExternoService(ServidorExternoClient servidorExternoClient) {
        this.servidorExternoClient = servidorExternoClient;
    }

    public ServidorExternoResponse  buscaServidorExterno(String matricula) {
        log.info("Buscando dados servidor no SARH: {}", matricula);
        Optional<ServidorExternoResponse> opt = servidorExternoClient.buscaDadosServidor(matricula);
        return opt.orElseThrow(()-> new ServidorExternoInexistenteException("Servidor '%s' inexistente".formatted(matricula)));
    }

}
