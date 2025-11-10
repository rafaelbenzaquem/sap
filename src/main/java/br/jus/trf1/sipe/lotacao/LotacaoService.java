package br.jus.trf1.sipe.lotacao;

import br.jus.trf1.sipe.lotacao.externo.jsarh.LotacaoExterna;
import org.springframework.stereotype.Service;

import java.util.Objects;
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

    public void atualizarLotacao(Lotacao lotacao, LotacaoExterna lotacaoExterna) {
        if(!Objects.equals(lotacao.getId(), lotacaoExterna.id())) {
            if(!lotacaoRepository.existsById(lotacaoExterna.id())){
                lotacao =  LotacaoMapping.toModel(lotacaoExterna);
                lotacaoRepository.save(lotacao);
            }
        }
    }
}

