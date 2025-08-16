package br.jus.trf1.sipe.externo.jsarh.lotacao.exceptions;

public class LotacaoExternaInexistenteException extends RuntimeException {

    public LotacaoExternaInexistenteException() {
    }

    public LotacaoExternaInexistenteException(String message) {
        super(message);
    }
}
