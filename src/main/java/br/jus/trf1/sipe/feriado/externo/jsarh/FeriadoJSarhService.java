package br.jus.trf1.sipe.feriado.externo.jsarh;

import br.jus.trf1.sipe.feriado.FeriadoService;
import br.jus.trf1.sipe.feriado.externo.jsarh.dto.FeriadoJSarhResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FeriadoJSarhService implements FeriadoService<FeriadoJSarh> {

    private final FeriadoJSarhClient feriadoJSarhClient;

    public FeriadoJSarhService(FeriadoJSarhClient feriadoJSarhClient) {
        this.feriadoJSarhClient = feriadoJSarhClient;
    }

    @Override
    public List<FeriadoJSarh> buscaFeriadosDoAno(int ano) {
        return feriadoJSarhClient.buscaFeriados(null, null, ano).stream().map(FeriadoJSarhResponse::toModel).toList();
    }

    @Override
    public List<FeriadoJSarh> buscaFeriadosDoPeriodo(LocalDate inicio, LocalDate fim) {
        return feriadoJSarhClient.buscaFeriados(inicio, fim, null).stream().map(FeriadoJSarhResponse::toModel).toList();
    }

    @Override
    public Optional<FeriadoJSarh> buscaFeriadoDoDia(LocalDate dia) {
        return feriadoJSarhClient.buscaFeriadoDoDia(dia).map(FeriadoJSarhResponse::toModel);
    }

}
