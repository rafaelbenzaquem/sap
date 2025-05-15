package br.jus.trf1.sipe.externo.jsarh.ausencias.especial;

import br.jus.trf1.sipe.externo.jsarh.ausencias.especial.dto.FrequencialEspecialExternoResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class FrequenciaEspecialExternoClientFallBackImpl implements FrequenciaEspecialClient {
    @Override
    public List<FrequencialEspecialExternoResponse> buscaAusenciasEspeciaisServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        return List.of();
    }

    @Override
    public Optional<FrequencialEspecialExternoResponse> buscaAusenciaEspecialServidorNoDia(String matricula, LocalDate dia) {
        return Optional.empty();
    }
}
