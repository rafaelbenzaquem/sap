package br.jus.trf1.sipe.ausencia.ausencia.domain.port.out;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AusenciaExternaPort {

    List<Ausencia> listaPorPeriodo(String matricula, LocalDate inicio, LocalDate fim);

    Optional<Ausencia> buscaNoDia(String matricula, LocalDate dia);
}
