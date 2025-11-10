package br.jus.trf1.sipe.ausencia.especial.externo.jsarh;

import br.jus.trf1.sipe.ausencia.especial.externo.jsarh.dto.FrequencialEspecialExternaResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class FrequenciaEspecialExternaClientFallBackImpl implements FrequenciaEspecialExternaClient {
    @Override
    public List<FrequencialEspecialExternaResponse> buscaAusenciasEspeciaisServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        return List.of();
    }

    @Override
    public Optional<FrequencialEspecialExternaResponse> buscaAusenciaEspecialServidorNoDia(String matricula, LocalDate dia) {
        return Optional.empty();
    }
}
