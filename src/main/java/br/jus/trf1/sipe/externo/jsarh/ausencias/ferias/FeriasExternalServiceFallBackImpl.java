package br.jus.trf1.sipe.externo.jsarh.ausencias.ferias;

import br.jus.trf1.sipe.externo.jsarh.ausencias.ferias.dto.FeriasExternalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FeriasExternalServiceFallBackImpl implements FeriasExternalClient {
    @Override
    public List<FeriasExternalResponse> buscaFeriasServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        log.info("buscaFeriasServidorPorPeriodo");
        return List.of();
    }

    @Override
    public Optional<FeriasExternalResponse> buscaFeriasServidorNoDia(String matricula, LocalDate dia) {
        log.info("buscaFeriasServidorNoDia");
        return Optional.empty();
    }
}
