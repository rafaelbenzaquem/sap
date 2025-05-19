package br.jus.trf1.sipe.externo.jsarh.feriado;

import br.jus.trf1.sipe.externo.jsarh.feriado.dto.FeriadoExternalResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class FeriadoExternalClientFallBackImpl implements FeriadoExternalClient {
    @Override
    public List<FeriadoExternalResponse> buscaFeriados(LocalDate inicio, LocalDate fim, Integer ano) {
        return List.of();
    }

    @Override
    public Optional<FeriadoExternalResponse> buscaFeriadoDoDia(LocalDate dia) {
        return Optional.empty();
    }
}
