package br.jus.trf1.sipe.ponto.domain.port.out;

import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.registro.domain.model.Registro;

import java.time.LocalDate;
import java.util.List;

public interface PontoPersistencePort {

    Boolean existePontosComAlteracaoRegistroPendentePorData(String matricula, LocalDate inicio, LocalDate fim);

    boolean existe(String matricula, LocalDate dia);

    Ponto busca(String matricula, LocalDate dia);

    List<Ponto> listaPorPeriodo(String matricula, LocalDate inicio, LocalDate fim);

    Ponto salva(Ponto ponto, List<Registro> registros);

    Ponto atualiza(Ponto ponto, List<Registro> registros);
}
