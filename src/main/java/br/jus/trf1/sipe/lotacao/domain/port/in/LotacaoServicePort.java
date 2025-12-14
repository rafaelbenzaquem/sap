package br.jus.trf1.sipe.lotacao.domain.port.in;

import br.jus.trf1.sipe.lotacao.domain.model.Lotacao;

import java.util.Set;

public interface LotacaoServicePort {
    Set<Integer> getLotacaos(Integer idLotacao);
    void atualizarLotacao(Lotacao lotacaoAtual, Lotacao lotacaoNova);
}