package br.jus.trf1.sipe.externo.jsarh.feriado;

import br.jus.trf1.sipe.externo.jsarh.feriado.dto.FeriadoResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class FeriadoServiceFallBackImpl implements FeriadoService {
    @Override
    public List<FeriadoResponse> buscaFeriados(LocalDate inicio, LocalDate fim, Integer ano) {
        return List.of();
    }

    @Override
    public Optional<FeriadoResponse> buscaFeriadoDoDia(LocalDate dia) {
        return Optional.empty();
    }
}
