package br.jus.trf1.sap.registro.exceptions;

public class RegistroInexistenteException extends RuntimeException {
    public RegistroInexistenteException(String message) {
        super(message);
    }

    public RegistroInexistenteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistroInexistenteException(Throwable cause) {
        super(cause);
    }

    public RegistroInexistenteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RegistroInexistenteException() {
    }
}
