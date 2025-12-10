package br.jus.trf1.sipe.lotacao;

import br.jus.trf1.sipe.lotacao.aplication.jsarh.LotacaoJSarh;
import br.jus.trf1.sipe.lotacao.infrastructure.persistence.LotacaoJpa;
import br.jus.trf1.sipe.lotacao.infrastructure.persistence.LotacaoJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LotacaoService {

    private LotacaoJpaRepository lotacaoRepository;

    public LotacaoService(LotacaoJpaRepository lotacaoRepository) {
        this.lotacaoRepository = lotacaoRepository;
    }


    public Set<Integer> getLotacaos(Integer idLotacao) {
        return lotacaoRepository.findLotacoesRecursivas(idLotacao).stream().map(LotacaoJpa::getId).collect(Collectors.toSet());
    }

    public void atualizarLotacao(LotacaoJpa lotacao, LotacaoJSarh lotacaoExterna) {
        if(!Objects.equals(lotacao.getId(), lotacaoExterna.id())) {
            if(!lotacaoRepository.existsById(lotacaoExterna.id())){
                lotacao =  LotacaoMapping.toEntity(lotacaoExterna);
                lotacaoRepository.save(lotacao);
            }
        }
    }
}

