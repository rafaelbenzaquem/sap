package br.jus.trf1.sipe.ponto.domain.port.in;

import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.ponto.infrastructure.jpa.PontoJpa;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface PontoServicePort {

    Boolean existePontoComPedidoAlteracaoPendenteNoPeriodo(String matricula, LocalDate inicio, LocalDate fim);

    boolean existe(String matricula, LocalDate dia);

    Ponto buscaPonto(String matricula, LocalDate dia);

    List<Ponto> buscarPontos(String matricula, LocalDate inicio, LocalDate fim);

    Ponto criaPonto(Ponto ponto);

    Ponto atualizaPonto(Ponto ponto);

    List<Ponto> carregaPontos(String matricula, LocalDate inicio, LocalDate fim);
}
