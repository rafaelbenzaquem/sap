package br.jus.trf1.sipe.ausencia.ausencia.domain.service;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.ausencia.ausencia.domain.port.in.AusenciaServicePort;
import br.jus.trf1.sipe.ausencia.ausencia.domain.port.out.AusenciaExternaPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AusenciaServiceAdapter implements AusenciaServicePort {

    private final AusenciaExternaPort ausenciaExternaPort;

    public AusenciaServiceAdapter(AusenciaExternaPort ausenciaExternaPort) {
        this.ausenciaExternaPort = ausenciaExternaPort;
    }

    @Override
    public List<Ausencia> buscaAusenciasServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        return ausenciaExternaPort.buscaAusenciasServidorPorPeriodo(matricula, inicio, fim);
    }

    @Override
    public Optional<Ausencia> buscaAusenciaServidorNoDia(String matricula, LocalDate dia) {
        return ausenciaExternaPort.buscaAusenciaServidorNoDia(matricula, dia);
    }


}
