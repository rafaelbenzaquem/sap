package br.jus.trf1.sap.ponto.exceptions;

public class PontoInexistenteException extends RuntimeException {

    public PontoInexistenteException() {
    }

    public PontoInexistenteException(String message) {
        super(message);
    }

    public PontoInexistenteException(String message, Throwable cause) {
        super(message, cause);
    }

    public PontoInexistenteException(Throwable cause) {
        super(cause);
    }

    public PontoInexistenteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
