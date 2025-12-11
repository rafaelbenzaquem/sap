package br.jus.trf1.sipe.lotacao.domain.port.out;

import br.jus.trf1.sipe.lotacao.application.jsarh.LotacaoJSarh;
import br.jus.trf1.sipe.lotacao.infrastructure.persistence.LotacaoJpa;
import java.util.Set;

public interface LotacaoPort {
    void atualizarLotacao(LotacaoJpa lotacao, LotacaoJSarh lotacaoJSarh);
    Set<Integer> getLotacoes(Integer idLotacaoPai);
}