package br.jus.trf1.sipe.registro.externo.coletor;

import br.jus.trf1.sipe.registro.externo.coletor.dto.RegistroExternalResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RegistroExternalClientFallBackImpl implements RegistroExternalClient{
    @Override
    public List<RegistroExternalResponse> buscarRegistrosDeAcesso(LocalDate inicio, LocalDate fim, String cracha, String nome, Integer codigo) {
        return List.of();
    }
}
