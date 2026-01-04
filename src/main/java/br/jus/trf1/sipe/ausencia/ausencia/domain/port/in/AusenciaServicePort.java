package br.jus.trf1.sipe.ausencia.ausencia.domain.port.in;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AusenciaServicePort {

    List<Ausencia> atualizaNoPeriodo(String matricula, LocalDate dataInicio, LocalDate dataFim);

    Optional<Ausencia> buscaNoDia(String matricula, LocalDate dia);
}
