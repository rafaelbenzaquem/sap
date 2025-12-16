package br.jus.trf1.sipe.ausencia.ferias.infrastructure.jsarh;

import br.jus.trf1.sipe.ausencia.ferias.domain.model.Ferias;
import br.jus.trf1.sipe.ausencia.ferias.domain.port.out.FeriasExternaPort;
import br.jus.trf1.sipe.ausencia.ferias.infrastructure.jsarh.dto.FeriasJSarhResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FeriasJSarhAdapter implements FeriasExternaPort {

    private final FeriasJSarhClient feriasJSarhClient;

    public FeriasJSarhAdapter(FeriasJSarhClient feriasJSarhClient) {
        this.feriasJSarhClient = feriasJSarhClient;
    }

    @Override
    public List<Ferias> buscaFeriasServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        return feriasJSarhClient.buscaFeriasServidorPorPeriodo(matricula, inicio, fim)
                .stream().map(FeriasJSarhResponse::toModel).toList();
    }

    @Override
    public Optional<Ferias> buscaFeriasServidorNoDia(String matricula, LocalDate dia) {
        return feriasJSarhClient.buscaFeriasServidorNoDia(matricula, dia).map(FeriasJSarhResponse::toModel);
    }
}
