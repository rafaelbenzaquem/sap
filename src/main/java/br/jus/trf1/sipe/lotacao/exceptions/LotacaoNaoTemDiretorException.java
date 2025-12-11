package br.jus.trf1.sipe.lotacao.exceptions;

public class LotacaoNaoTemDiretorException extends RuntimeException {

    public LotacaoNaoTemDiretorException(Integer idLotacao) {
        this("Lotação id: %d não tem diretor!".formatted(idLotacao));
    }

    public LotacaoNaoTemDiretorException(String message) {
        super(message);
    }
}
