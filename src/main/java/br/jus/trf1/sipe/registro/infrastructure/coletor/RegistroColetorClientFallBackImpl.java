package br.jus.trf1.sipe.registro.infrastructure.coletor;

import br.jus.trf1.sipe.registro.infrastructure.coletor.dto.RegistroColetorResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RegistroColetorClientFallBackImpl implements RegistroColetorClient {
    @Override
    public List<RegistroColetorResponse> buscarRegistrosDeAcesso(LocalDate inicio, LocalDate fim, String cracha, String nome, Integer codigo) {
        return List.of();
    }
}
