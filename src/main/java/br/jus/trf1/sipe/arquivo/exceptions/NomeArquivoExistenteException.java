package br.jus.trf1.sipe.arquivo.exceptions;

public class NomeArquivoExistenteException extends RuntimeException{

    public NomeArquivoExistenteException(String message) {
        super(message);
    }
}
