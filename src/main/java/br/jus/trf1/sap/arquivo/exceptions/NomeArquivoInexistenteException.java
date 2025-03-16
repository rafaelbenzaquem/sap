package br.jus.trf1.sap.arquivo.exceptions;

public class NomeArquivoInexistenteException extends RuntimeException{

    public NomeArquivoInexistenteException(String message) {
        super(message);
    }
}
