package br.jus.trf1.sipe.servidor.domain.port.out;

import br.jus.trf1.sipe.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import java.time.LocalDate;
import java.util.List;

public interface AusenciaRepositoryPort {
    List<Ausencia> listaAusenciasPorServidorMaisPeriodo(Servidor servidor, LocalDate dataInicio, LocalDate dataFim);
    void deleteAll(List<Ausencia> ausencias);
    void save(Ausencia ausencia);
}