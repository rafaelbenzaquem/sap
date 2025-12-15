package br.jus.trf1.sipe.feriado.infrastructure.jsarh;

import br.jus.trf1.sipe.feriado.domain.model.Feriado;
import br.jus.trf1.sipe.feriado.domain.port.out.FeriadoExternoPort;
import br.jus.trf1.sipe.feriado.infrastructure.jsarh.dto.FeriadoJSarhResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FeriadoJSarhAdapter implements FeriadoExternoPort {

    private final FeriadoJSarhClient feriadoJSarhClient;

    public FeriadoJSarhAdapter(FeriadoJSarhClient feriadoJSarhClient) {
        this.feriadoJSarhClient = feriadoJSarhClient;
    }

    @Override
    public List<Feriado> buscaFeriadosDoAno(int ano) {
        return feriadoJSarhClient.buscaFeriados(null, null, ano).stream().map(FeriadoJSarhResponse::toModel).toList();
    }

    @Override
    public List<Feriado> buscaFeriadosDoPeriodo(LocalDate inicio, LocalDate fim) {
        return feriadoJSarhClient.buscaFeriados(inicio, fim, null).stream().map(FeriadoJSarhResponse::toModel).toList();
    }

    @Override
    public Optional<Feriado> buscaFeriadoDoDia(LocalDate dia) {
        return feriadoJSarhClient.buscaFeriadoDoDia(dia).map(FeriadoJSarhResponse::toModel);
    }

}
