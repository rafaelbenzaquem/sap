package br.jus.trf1.sipe.servidor.domain.port.in;

import br.jus.trf1.sipe.servidor.domain.model.Servidor;

import java.util.Optional;

public interface ServidorExternoPort {

    Optional<Servidor> buscaServidorExterno(String matricula);

}
