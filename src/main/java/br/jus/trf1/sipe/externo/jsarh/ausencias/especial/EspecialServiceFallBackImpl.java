package br.jus.trf1.sipe.externo.jsarh.ausencias.especial;

import br.jus.trf1.sipe.externo.jsarh.ausencias.especial.dto.EspecialResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class EspecialServiceFallBackImpl implements EspecialService {
    @Override
    public List<EspecialResponse> buscaAusenciasEspeciaisServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        return List.of();
    }

    @Override
    public Optional<EspecialResponse> buscaAusenciaEspecialServidorNoDia(String matricula, LocalDate dia) {
        return Optional.empty();
    }
}
