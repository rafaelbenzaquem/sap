package br.jus.trf1.sipe.folha.domain.port.out;

import br.jus.trf1.sipe.folha.domain.model.Folha;

import java.util.Optional;

public interface FolhaPersistencePort {

    Folha salva(Folha folha);

    Optional<Folha> buscaFolha(String matricula, Integer valor, int ano);
}
