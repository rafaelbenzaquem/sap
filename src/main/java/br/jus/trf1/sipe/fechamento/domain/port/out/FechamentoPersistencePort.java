package br.jus.trf1.sipe.fechamento.domain.port.out;

import br.jus.trf1.sipe.fechamento.domain.model.Fechamento;

import java.util.Optional;

public interface FechamentoPersistencePort {

    Optional<Fechamento> buscaPorMatriculaMesAno(String matricula, Integer valorMes, Integer ano);

    Fechamento salva(Fechamento fechamento);

}
