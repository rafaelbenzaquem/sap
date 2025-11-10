package br.jus.trf1.sipe.lotacao.externo.jsarh.exceptions;

public class LotacaoExternaInexistenteException extends RuntimeException {

    public LotacaoExternaInexistenteException() {
    }

    public LotacaoExternaInexistenteException(String message) {
        super(message);
    }
}
