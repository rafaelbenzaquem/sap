package br.jus.trf1.sap.vinculo;

public class VinculoInexistenteException extends RuntimeException {

    public VinculoInexistenteException() {
    }

    public VinculoInexistenteException(String message) {
        super(message);
    }

    public VinculoInexistenteException(String message, Throwable cause) {
        super(message, cause);
    }

    public VinculoInexistenteException(Throwable cause) {
        super(cause);
    }

    public VinculoInexistenteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
