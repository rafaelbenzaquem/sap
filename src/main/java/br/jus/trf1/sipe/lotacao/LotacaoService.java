package br.jus.trf1.sipe.lotacao;

import br.jus.trf1.sipe.lotacao.externo.jsarh.LotacaoExterna;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LotacaoService {

    private final LotacaoRepository lotacaoRepository;

    public LotacaoService(LotacaoRepository lotacaoRepository) {
        this.lotacaoRepository = lotacaoRepository;
    }


    public Set<Integer> getIdsLotacoes(Integer idLotacao) {
        return lotacaoRepository.findLotacoesRecursivas(idLotacao).stream().map(Lotacao::getId).collect(Collectors.toSet());
    }

    public Set<Lotacao> getLotacoes(Integer idLotacao) {
        return lotacaoRepository.findLotacoesRecursivas(idLotacao);
    }

    public void atualizarLotacao(Lotacao lotacao, LotacaoExterna lotacaoExterna) {
        if(lotacao==null||!Objects.equals(lotacao.getId(), lotacaoExterna.id())) {
            if(!lotacaoRepository.existsById(lotacaoExterna.id())){
                lotacao =  LotacaoMapping.toModel(lotacaoExterna);
                lotacaoRepository.save(lotacao);
            }
        }
    }
}

