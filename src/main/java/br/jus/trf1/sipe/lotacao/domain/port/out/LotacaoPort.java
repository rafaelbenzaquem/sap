package br.jus.trf1.sipe.lotacao.domain.port.out;

import br.jus.trf1.sipe.lotacao.domain.model.Lotacao;
import java.util.Set;

public interface LotacaoPort {
    void atualizarLotacao(Lotacao lotacao);
    Set<Integer> getLotacaos(Integer idLotacaoPai);
}