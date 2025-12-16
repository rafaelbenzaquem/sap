package br.jus.trf1.sipe.ausencia.ferias.domain.port.out;

import br.jus.trf1.sipe.ausencia.ferias.domain.model.Ferias;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FeriasExternaPort {

    List<Ferias> buscaFeriasServidorPorPeriodo(String matricula,
                                               LocalDate inicio,
                                               LocalDate fim);

    Optional<Ferias> buscaFeriasServidorNoDia(String matricula,
                                              LocalDate dia);

}
