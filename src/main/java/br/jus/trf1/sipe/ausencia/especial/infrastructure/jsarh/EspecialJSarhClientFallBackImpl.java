package br.jus.trf1.sipe.ausencia.especial.infrastructure.jsarh;

import br.jus.trf1.sipe.ausencia.especial.infrastructure.jsarh.dto.EspecialJSarhResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class EspecialJSarhClientFallBackImpl implements EspecialJSarhClient {
    @Override
    public List<EspecialJSarhResponse> buscaAusenciasEspeciaisServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        return List.of();
    }

    @Override
    public Optional<EspecialJSarhResponse> buscaAusenciaEspecialServidorNoDia(String matricula, LocalDate dia) {
        return Optional.empty();
    }
}
