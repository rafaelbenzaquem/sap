package br.jus.trf1.sipe.servidor.domain.port.out;

import br.jus.trf1.sipe.servidor.aplication.jsarh.dto.ServidorJSarhResponse;

import java.util.Optional;

public interface ServidorJSarhPort {
    Optional<ServidorJSarhResponse> buscaServidorExterno(String matricula);
}