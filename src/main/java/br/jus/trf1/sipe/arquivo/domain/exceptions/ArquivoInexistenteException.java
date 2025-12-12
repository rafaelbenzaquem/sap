package br.jus.trf1.sipe.arquivo.domain.exceptions;

public class ArquivoInexistenteException extends RuntimeException{

    public ArquivoInexistenteException(String message) {
        super(message);
    }
}
