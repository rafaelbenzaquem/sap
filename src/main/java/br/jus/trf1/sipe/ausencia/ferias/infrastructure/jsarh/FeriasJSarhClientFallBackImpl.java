package br.jus.trf1.sipe.ausencia.ferias.infrastructure.jsarh;

import br.jus.trf1.sipe.ausencia.ferias.infrastructure.jsarh.dto.FeriasJSarhResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FeriasJSarhClientFallBackImpl implements FeriasJSarhClient {
    @Override
    public List<FeriasJSarhResponse> buscaFeriasServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        log.info("buscaFeriasServidorPorPeriodo");
        return List.of();
    }

    @Override
    public Optional<FeriasJSarhResponse> buscaFeriasServidorNoDia(String matricula, LocalDate dia) {
        log.info("buscaFeriasServidorNoDia");
        return Optional.empty();
    }
}
