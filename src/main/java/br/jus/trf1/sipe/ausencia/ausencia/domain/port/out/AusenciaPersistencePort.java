package br.jus.trf1.sipe.ausencia.ausencia.domain.port.out;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;

import java.time.LocalDate;
import java.util.List;

public interface AusenciaPersistencePort {

    List<Ausencia> listaAusenciasPorServidorMaisPeriodo(String matricula, LocalDate dataInicio, LocalDate dataFim);

    void deletaTodos(List<Ausencia> ausenciasParaDelete);

    Ausencia salva(Ausencia ausencia);
}
