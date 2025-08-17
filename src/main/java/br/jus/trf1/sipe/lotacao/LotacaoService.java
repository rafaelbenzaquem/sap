package br.jus.trf1.sipe.lotacao;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LotacaoService {

    private LotacaoRepository lotacaoRepository;

    public LotacaoService(LotacaoRepository lotacaoRepository) {
        this.lotacaoRepository = lotacaoRepository;
    }


    public Set<Integer> getLotacaos(Integer idLotacao) {
        return lotacaoRepository.findLotacoesRecursivas(idLotacao).stream().map(Lotacao::getId).collect(Collectors.toSet());
    }

}
