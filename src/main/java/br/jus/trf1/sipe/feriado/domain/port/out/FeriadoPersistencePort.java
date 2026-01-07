package br.jus.trf1.sipe.feriado.domain.port.out;

import br.jus.trf1.sipe.feriado.domain.model.Feriado;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FeriadoPersistencePort {

    Optional<Feriado> busca(LocalDate data);

    List<Feriado> listaPorPeriodo(LocalDate inicio, LocalDate fim);

    void apaga(LocalDate data);

    List<Feriado> listaPorAno(int ano);

    List<Feriado> substitui(List<Feriado> feriadosAtuais, List<Feriado> feriadosNovos);
}
