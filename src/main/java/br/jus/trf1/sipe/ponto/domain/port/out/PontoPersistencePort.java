package br.jus.trf1.sipe.ponto.domain.port.out;

import br.jus.trf1.sipe.ponto.domain.model.Ponto;

import java.time.LocalDate;
import java.util.List;

public interface PontoPersistencePort {

    Boolean existePontosComAlteracaoRegistroPendentePorData(String matricula, LocalDate inicio, LocalDate fim);

    boolean existe(String matricula, LocalDate dia);

    Ponto busca(String matricula, LocalDate dia);

    List<Ponto> buscaPontosPorPeriodo(String matricula, LocalDate inicio, LocalDate fim);

    Ponto salva(Ponto ponto);
}
