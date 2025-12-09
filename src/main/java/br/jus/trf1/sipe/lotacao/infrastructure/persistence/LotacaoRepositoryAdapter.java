package br.jus.trf1.sipe.lotacao.infrastructure.persistence;

import br.jus.trf1.sipe.lotacao.domain.model.Lotacao;
import br.jus.trf1.sipe.lotacao.domain.port.out.LotacaoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LotacaoRepositoryAdapter implements LotacaoRepositoryPort {

    private final LotacaoJpaRepository repository;

    @Override
    public Set<Lotacao> findLotacoesRecursivas(Integer lotacaoId) {
        return repository.findLotacoesRecursivas(lotacaoId);
    }

    @Override
    public Optional<Lotacao> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Lotacao save(Lotacao lotacao) {
        return repository.save(lotacao);
    }
}