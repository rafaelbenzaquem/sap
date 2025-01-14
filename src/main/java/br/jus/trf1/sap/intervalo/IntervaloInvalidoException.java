package br.jus.trf1.sap.intervalo;

public class IntervaloInvalidoException extends RuntimeException{

    public IntervaloInvalidoException() {
    }

    public IntervaloInvalidoException(String message) {
        super(message);
    }

    public IntervaloInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public IntervaloInvalidoException(Throwable cause) {
        super(cause);
    }

    public IntervaloInvalidoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
