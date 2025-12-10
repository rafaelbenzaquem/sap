package br.jus.trf1.sipe.lotacao.domain.port.in;

import br.jus.trf1.sipe.lotacao.aplication.jsarh.LotacaoJSarh;
import br.jus.trf1.sipe.lotacao.infrastructure.persistence.LotacaoJpa;
import java.util.Set;

public interface LotacaoServicePort {
    Set<Integer> getLotacaos(Integer idLotacao);
    void atualizarLotacao(LotacaoJpa lotacao, LotacaoJSarh lotacaoJSarh);
}