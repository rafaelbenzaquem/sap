package br.jus.trf1.sipe.externo.jsarh.ausencias.licenca;

import br.jus.trf1.sipe.externo.jsarh.ausencias.licenca.dto.LicencaExternalResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class LicencaExternalClientFallBackImpl implements LicencaExternalClient {
    @Override
    public List<LicencaExternalResponse> buscaLicencaServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        return List.of();
    }

    @Override
    public Optional<LicencaExternalResponse> buscaLicencaServidorNoDia(String matricula, LocalDate dia) {
        return Optional.empty();
    }
}
