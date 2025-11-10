package br.jus.trf1.sipe.ausencia.ferias.externo.jsarh;

import br.jus.trf1.sipe.ausencia.ferias.externo.jsarh.dto.FeriasExternasResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FeriasExternasClientFallBackImpl implements FeriasExternasClient {
    @Override
    public List<FeriasExternasResponse> buscaFeriasServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        log.info("buscaFeriasServidorPorPeriodo");
        return List.of();
    }

    @Override
    public Optional<FeriasExternasResponse> buscaFeriasServidorNoDia(String matricula, LocalDate dia) {
        log.info("buscaFeriasServidorNoDia");
        return Optional.empty();
    }
}
