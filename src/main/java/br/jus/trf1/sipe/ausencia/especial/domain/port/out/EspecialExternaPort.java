package br.jus.trf1.sipe.ausencia.especial.domain.port.out;

import br.jus.trf1.sipe.ausencia.especial.domain.model.Especial;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EspecialExternaPort {
    List<Especial> buscaAusenciasEspeciaisServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim);

    Optional<Especial> buscaAusenciaEspecialServidorNoDia(String matricula, LocalDate dia);
}
