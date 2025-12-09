package br.jus.trf1.sipe.lotacao.domain.port.out;

import br.jus.trf1.sipe.lotacao.domain.model.Lotacao;
import java.util.Optional;
import java.util.Set;

public interface LotacaoRepositoryPort {
    Set<Lotacao> findLotacoesRecursivas(Integer lotacaoId);
    Optional<Lotacao> findById(Integer id);
    Lotacao save(Lotacao lotacao);
}