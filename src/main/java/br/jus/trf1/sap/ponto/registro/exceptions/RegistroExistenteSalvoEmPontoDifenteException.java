package br.jus.trf1.sap.ponto.registro.exceptions;

public class RegistroExistenteSalvoEmPontoDifenteException extends RuntimeException {
    public RegistroExistenteSalvoEmPontoDifenteException(String message) {
        super(message);
    }

    public RegistroExistenteSalvoEmPontoDifenteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistroExistenteSalvoEmPontoDifenteException(Throwable cause) {
        super(cause);
    }

    public RegistroExistenteSalvoEmPontoDifenteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RegistroExistenteSalvoEmPontoDifenteException() {
    }
}
