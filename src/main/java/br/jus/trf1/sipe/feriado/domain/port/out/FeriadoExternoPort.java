package br.jus.trf1.sipe.feriado.domain.port.out;

import br.jus.trf1.sipe.feriado.domain.model.Feriado;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FeriadoExternoPort {

    List<Feriado> buscaFeriadosDoAno(int ano);

    List<Feriado> buscaFeriadosDoPeriodo(LocalDate inicio, LocalDate fim);

    Optional<Feriado> buscaFeriadoDoDia(LocalDate dia);
}
