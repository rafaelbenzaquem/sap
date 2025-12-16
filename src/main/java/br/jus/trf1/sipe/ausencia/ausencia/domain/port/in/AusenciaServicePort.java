package br.jus.trf1.sipe.ausencia.ausencia.domain.port.in;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AusenciaServicePort {

    List<Ausencia> buscaAusenciasServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim);

    Optional<Ausencia> buscaAusenciaServidorNoDia(String matricula, LocalDate dia);
}
