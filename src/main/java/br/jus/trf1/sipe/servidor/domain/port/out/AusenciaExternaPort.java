package br.jus.trf1.sipe.servidor.domain.port.out;

import br.jus.trf1.sipe.ausencia.externo.jsrh.to.AusenciaExternaTO; // Mantendo o TO
import java.time.LocalDate;
import java.util.List;

public interface AusenciaExternaPort {
    List<AusenciaExternaTO> buscaAusenciasServidorPorPeriodo(String matricula, LocalDate dataInicio, LocalDate dataFim);
}