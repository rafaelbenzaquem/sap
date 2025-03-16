package br.jus.trf1.sap.arquivo.exceptions;

public class NomeArquivoExistenteException extends RuntimeException{

    public NomeArquivoExistenteException(String message) {
        super(message);
    }
}
