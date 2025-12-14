package br.jus.trf1.sipe.lotacao.domain.port.out;

import br.jus.trf1.sipe.lotacao.domain.model.Lotacao;

import java.util.Optional;
import java.util.Set;

public interface LotacaoRepositoryPort {
    Set<Lotacao> buscaLotacoesRecursivas(Integer lotacaoId);
    Optional<Lotacao> buscaPorId(Integer id);
    Lotacao salva(Lotacao lotacao);
}