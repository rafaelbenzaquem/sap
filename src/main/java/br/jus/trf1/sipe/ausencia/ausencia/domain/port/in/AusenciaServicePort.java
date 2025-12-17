package br.jus.trf1.sipe.ausencia.ausencia.domain.port.in;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;

import java.time.LocalDate;
import java.util.List;

public interface AusenciaServicePort {

    List<Ausencia> atualizaAusencias(String matricula, LocalDate dataInicio, LocalDate dataFim);
}
