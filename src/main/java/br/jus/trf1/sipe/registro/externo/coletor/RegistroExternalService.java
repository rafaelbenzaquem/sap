package br.jus.trf1.sipe.registro.externo.coletor;

import br.jus.trf1.sipe.registro.externo.coletor.dto.RegistroExternalResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegistroExternalService {

    private final RegistroExternalClient registroExternalClient;

    public RegistroExternalService(RegistroExternalClient registroExternalClient) {
        this.registroExternalClient = registroExternalClient;
    }

    public List<RegistroExternal> buscaRegistrosDoDiaPorCracha(LocalDate dia, Integer cracha) {
        return registroExternalClient.buscarRegistrosDeAcesso(
                        dia, null, String.format("%016d", cracha), null, null).stream()
                .map(RegistroExternalResponse::toModel).toList();
    }
}
