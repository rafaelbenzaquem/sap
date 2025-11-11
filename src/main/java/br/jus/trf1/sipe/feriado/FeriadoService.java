package br.jus.trf1.sipe.feriado;

import br.jus.trf1.sipe.feriado.externo.jsarh.FeriadoJSarh;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FeriadoService<E extends Feriado> {

    List<E> buscaFeriadosDoAno(int ano);

    List<E> buscaFeriadosDoPeriodo(LocalDate inicio, LocalDate fim);

    Optional<E> buscaFeriadoDoDia(LocalDate dia);

}
