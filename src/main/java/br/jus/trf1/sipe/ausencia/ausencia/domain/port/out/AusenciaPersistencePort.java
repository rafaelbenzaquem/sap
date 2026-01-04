package br.jus.trf1.sipe.ausencia.ausencia.domain.port.out;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AusenciaPersistencePort {

    List<Ausencia> listaPorPeriodo(String matricula, LocalDate dataInicio, LocalDate dataFim);

    Ausencia salva(Ausencia ausencia);

    List<Ausencia> substituiTodos(List<Ausencia> ausenciasParaDelete, List<Ausencia> ausenciasParaSalve);

    List<Ausencia> salvaTodos(List<Ausencia> ausenciasNovas);

    Optional<Ausencia> buscaNoDia(String matricula, LocalDate dia);

    Ausencia substitui(Ausencia ausenciaAntiga, Ausencia ausenciaNova);
}
