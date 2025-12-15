package br.jus.trf1.sipe.feriado.domain.service;

import br.jus.trf1.sipe.feriado.domain.model.Feriado;
import br.jus.trf1.sipe.feriado.domain.port.in.FeriadoServicePort;
import br.jus.trf1.sipe.feriado.domain.port.out.FeriadoExternoPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FeriadoServiceAdapter implements FeriadoServicePort {

    private final FeriadoExternoPort feriadoExternoPort;

    public FeriadoServiceAdapter(FeriadoExternoPort feriadoExternoPort) {
        this.feriadoExternoPort = feriadoExternoPort;
    }

    @Override
    public List<Feriado> buscaFeriadosDoAno(int ano) {
        return feriadoExternoPort.buscaFeriadosDoAno(ano);
    }

    @Override
    public List<Feriado> buscaFeriadosDoPeriodo(LocalDate inicio, LocalDate fim) {
        return feriadoExternoPort.buscaFeriadosDoPeriodo(inicio, fim);
    }

    @Override
    public Optional<Feriado> buscaFeriadoDoDia(LocalDate dia) {
        return feriadoExternoPort.buscaFeriadoDoDia(dia);
    }
}
