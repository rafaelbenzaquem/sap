package br.jus.trf1.sipe.folha.domain.port.in;

import br.jus.trf1.sipe.folha.domain.model.Folha;
import br.jus.trf1.sipe.folha.domain.model.Mes;

import java.util.Optional;

public interface FolhaServicePort {
    Folha abrirFolha(String matricula, Mes mes, int ano);

    Optional<Folha> buscarFolha(String matricula, Mes mes, int ano);
}
