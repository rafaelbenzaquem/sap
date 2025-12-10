package br.jus.trf1.sipe.lotacao.domain.port.out;

import br.jus.trf1.sipe.lotacao.infrastructure.persistence.LotacaoJpa;
import java.util.Optional;
import java.util.Set;

public interface LotacaoRepositoryPort {
    Set<LotacaoJpa> findLotacoesRecursivas(Integer lotacaoId);
    Optional<LotacaoJpa> findById(Integer id);
    LotacaoJpa save(LotacaoJpa lotacao);
}