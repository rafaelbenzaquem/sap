package br.jus.trf1.sipe.ausencia.licenca.infrastructure.jsarh;

import br.jus.trf1.sipe.ausencia.licenca.domain.model.Licenca;
import br.jus.trf1.sipe.ausencia.licenca.domain.port.out.LicencaExternaPort;
import br.jus.trf1.sipe.ausencia.licenca.infrastructure.jsarh.dto.LicencaExternoResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LicencaJSarhAdapter implements LicencaExternaPort {

    private final LicencaJSarhClient licencaJSarhClient;

    public LicencaJSarhAdapter(LicencaJSarhClient licencaJSarhClient) {
        this.licencaJSarhClient = licencaJSarhClient;
    }

    @Override
    public List<Licenca> buscaLicencaServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        return licencaJSarhClient.buscaLicencaServidorPorPeriodo(matricula, inicio, fim)
                .stream().map(LicencaExternoResponse::toModel).toList();
    }

    @Override
    public Optional<Licenca> buscaLicencaServidorNoDia(String matricula, LocalDate dia) {
        return licencaJSarhClient.buscaLicencaServidorNoDia(matricula, dia).map(LicencaExternoResponse::toModel);
    }
}
