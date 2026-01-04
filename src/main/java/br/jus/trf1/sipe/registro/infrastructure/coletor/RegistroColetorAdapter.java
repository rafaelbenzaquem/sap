package br.jus.trf1.sipe.registro.infrastructure.coletor;

import br.jus.trf1.sipe.registro.domain.model.Registro;
import br.jus.trf1.sipe.registro.domain.port.out.RegistroExternoPort;
import br.jus.trf1.sipe.registro.infrastructure.coletor.dto.RegistroColetorResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegistroColetorAdapter implements RegistroExternoPort {

    private final RegistroColetorClient registroColetorClient;

    public RegistroColetorAdapter(RegistroColetorClient registroColetorClient) {
        this.registroColetorClient = registroColetorClient;
    }

    public List<Registro> buscaRegistrosDoDiaPorCracha(LocalDate dia, Integer cracha) {
        return registroColetorClient.buscarRegistrosDeAcesso(
                        dia, null, String.format("%016d", cracha), null, null).stream()
                .map(RegistroColetorResponse::toDomain).toList();
    }
}
