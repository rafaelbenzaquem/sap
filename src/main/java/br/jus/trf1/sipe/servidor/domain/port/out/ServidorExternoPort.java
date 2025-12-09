package br.jus.trf1.sipe.servidor.domain.port.out;

import br.jus.trf1.sipe.servidor.externo.jsarh.to.ServidorExternoTO; // Mantendo o TO por simplicidade
import java.util.Optional;

public interface ServidorExternoPort {
    Optional<ServidorExternoTO> buscaServidorExterno(String matricula);
}