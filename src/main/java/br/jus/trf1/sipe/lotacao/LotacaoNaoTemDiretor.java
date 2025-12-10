package br.jus.trf1.sipe.lotacao;

public class LotacaoNaoTemDiretor extends RuntimeException {

    public LotacaoNaoTemDiretor(Integer idLotacao) {
        this("Lotação id: %d não tem diretor!".formatted(idLotacao));
    }

    public LotacaoNaoTemDiretor(String message) {
        super(message);
    }
}
