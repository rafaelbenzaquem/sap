package br.jus.trf1.sap.arquivo;

public class ArquivoInexistenteException extends RuntimeException {
    public ArquivoInexistenteException() {
    }

    public ArquivoInexistenteException(String message) {
        super(message);
    }

    public ArquivoInexistenteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArquivoInexistenteException(Throwable cause) {
        super(cause);
    }

    public ArquivoInexistenteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
