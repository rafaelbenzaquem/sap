package br.jus.trf1.sipe.lotacao;

public class LotacaoNaoTemDiretorDireto extends RuntimeException {

    public LotacaoNaoTemDiretorDireto(Integer idLotacao) {
        this("Lotação id: %d não tem diretor direto!".formatted(idLotacao));
    }

    public LotacaoNaoTemDiretorDireto(String message) {
        super(message);
    }
}
