package br.jus.trf1.sap.ponto.exceptions;

public class PontoInexistenteException extends RuntimeException {


    public PontoInexistenteException(String matricula, String dia) {
        this("Não existe uma ponto com id:{\"matricula\":\"%s\",\"dia\":\"%s\"}".formatted(matricula, dia));
    }
    public PontoInexistenteException(String message) {
        super(message);
    }
}
