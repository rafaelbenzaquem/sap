package br.jus.trf1.sipe.lotacao.infrastructure.persistence;

import br.jus.trf1.sipe.lotacao.domain.port.out.LotacaoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LotacaoJpaRepositoryAdapter implements LotacaoRepositoryPort {

    private final LotacaoJpaRepository repository;

    @Override
    public Set<LotacaoJpa> findLotacoesRecursivas(Integer lotacaoId) {
        return repository.findLotacoesRecursivas(lotacaoId);
    }

    @Override
    public Optional<LotacaoJpa> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public LotacaoJpa save(LotacaoJpa lotacao) {
        return repository.save(lotacao);
    }
}