package br.jus.trf1.sap.ponto.exceptions;

public class PontoExistenteException extends RuntimeException {

    public PontoExistenteException(String matricula, String dia) {
        this("Já existe uma ponto com id:{\"matricula\":\"%s\",\"dia\":\"%s\"}".formatted(matricula, dia));
    }

    public PontoExistenteException(String message) {
        super(message);
    }
}
