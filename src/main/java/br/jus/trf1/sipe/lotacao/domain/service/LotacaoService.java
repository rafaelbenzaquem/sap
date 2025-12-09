package br.jus.trf1.sipe.lotacao.domain.service;

import br.jus.trf1.sipe.lotacao.domain.model.Lotacao;
import br.jus.trf1.sipe.lotacao.domain.port.in.LotacaoServicePort;
import br.jus.trf1.sipe.lotacao.domain.port.out.LotacaoRepositoryPort;
import br.jus.trf1.sipe.lotacao.externo.jsarh.to.LotacaoExternaTO;
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
                .map(Lotacao::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public void atualizarLotacao(Lotacao lotacao, LotacaoExternaTO lotacaoExterna) {
        // Se a lotação atual for nula ou os IDs forem diferentes, precisamos verificar/criar a nova.
        if (lotacao == null || !Objects.equals(lotacao.getId(), lotacaoExterna.id())) {
            // Se a lotação externa não existir no nosso banco, crie-a.
            if (lotacaoRepository.findById(lotacaoExterna.id()).isEmpty()) {
                Lotacao novaLotacao = LotacaoMapping.toModel(lotacaoExterna);
                lotacaoRepository.save(novaLotacao);
            }
        }
    }
}