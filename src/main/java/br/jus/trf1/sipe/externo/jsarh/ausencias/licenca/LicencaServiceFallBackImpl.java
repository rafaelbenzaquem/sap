package br.jus.trf1.sipe.externo.jsarh.ausencias.licenca;

import br.jus.trf1.sipe.externo.jsarh.ausencias.licenca.dto.LicencaResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class LicencaServiceFallBackImpl implements LicencasService {
    @Override
    public List<LicencaResponse> buscaLicencaServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        return List.of();
    }

    @Override
    public Optional<LicencaResponse> buscaLicencaServidorNoDia(String matricula, LocalDate dia) {
        return Optional.empty();
    }
}
