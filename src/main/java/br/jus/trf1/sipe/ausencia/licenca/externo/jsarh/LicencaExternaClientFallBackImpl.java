package br.jus.trf1.sipe.ausencia.licenca.externo.jsarh;

import br.jus.trf1.sipe.ausencia.licenca.externo.jsarh.dto.LicencaExternoResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class LicencaExternaClientFallBackImpl implements LicencaExternaClient {
    @Override
    public List<LicencaExternoResponse> buscaLicencaServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        return List.of();
    }

    @Override
    public Optional<LicencaExternoResponse> buscaLicencaServidorNoDia(String matricula, LocalDate dia) {
        return Optional.empty();
    }
}
