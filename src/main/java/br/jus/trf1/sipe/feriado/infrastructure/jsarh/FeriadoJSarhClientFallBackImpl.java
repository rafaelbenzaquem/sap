package br.jus.trf1.sipe.feriado.infrastructure.jsarh;

import br.jus.trf1.sipe.feriado.infrastructure.jsarh.dto.FeriadoJSarhResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class FeriadoJSarhClientFallBackImpl implements FeriadoJSarhClient {
    @Override
    public List<FeriadoJSarhResponse> buscaFeriados(LocalDate inicio, LocalDate fim, Integer ano) {
        return List.of();
    }

    @Override
    public Optional<FeriadoJSarhResponse> buscaFeriadoDoDia(LocalDate dia) {
        return Optional.empty();
    }
}
