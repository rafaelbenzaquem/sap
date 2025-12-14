package br.jus.trf1.sipe.lotacao.infrastructure.persistence;

import br.jus.trf1.sipe.lotacao.domain.model.Lotacao;
import br.jus.trf1.sipe.lotacao.domain.port.out.LotacaoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LotacaoJpaRepositoryAdapter implements LotacaoRepositoryPort {

    private final LotacaoJpaRepository repository;

    @Override
    public Set<Lotacao> buscaLotacoesRecursivas(Integer lotacaoId) {
        return repository.findLotacoesRecursivas(lotacaoId).stream().map(LotacaoJpaMapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Lotacao> buscaPorId(Integer id) {
        return repository.findById(id).map(LotacaoJpaMapper::toDomain);
    }

    @Override
    public Lotacao salva(Lotacao lotacao) {
        var lotacaoJpa = LotacaoJpaMapper.toEntity(lotacao);
        lotacaoJpa = repository.save(lotacaoJpa);
        return LotacaoJpaMapper.toDomain(lotacaoJpa);
    }
}