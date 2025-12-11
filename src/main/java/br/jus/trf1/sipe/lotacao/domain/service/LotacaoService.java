package br.jus.trf1.sipe.lotacao.domain.service;

import br.jus.trf1.sipe.lotacao.application.jsarh.LotacaoJSarhMapper;
import br.jus.trf1.sipe.lotacao.application.jsarh.LotacaoJSarh;
import br.jus.trf1.sipe.lotacao.infrastructure.persistence.LotacaoJpa;
import br.jus.trf1.sipe.lotacao.domain.port.in.LotacaoServicePort;
import br.jus.trf1.sipe.lotacao.domain.port.out.LotacaoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LotacaoService implements LotacaoServicePort {

    private final LotacaoRepositoryPort lotacaoRepository;

    @Override
    public Set<Integer> getLotacaos(Integer idLotacao) {
        return lotacaoRepository.findLotacoesRecursivas(idLotacao).stream()
                .map(LotacaoJpa::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public void atualizarLotacao(LotacaoJpa lotacao, LotacaoJSarh lotacaoExterna) {
        if (lotacao == null || !Objects.equals(lotacao.getId(), lotacaoExterna.id())) {
            if (lotacaoRepository.findById(lotacaoExterna.id()).isEmpty()) {
                LotacaoJpa novaLotacao = LotacaoJSarhMapper.toEntity(lotacaoExterna);
                lotacaoRepository.save(novaLotacao);
            }
        }
    }
}