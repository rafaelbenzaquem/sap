package br.jus.trf1.sipe.lotacao.infrastructure;

import br.jus.trf1.sipe.lotacao.aplication.jsarh.LotacaoJSarh;
import br.jus.trf1.sipe.lotacao.infrastructure.persistence.LotacaoJpa;
import br.jus.trf1.sipe.lotacao.domain.port.in.LotacaoServicePort;
import br.jus.trf1.sipe.lotacao.domain.port.out.LotacaoPort; // A porta que este adaptador implementa
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class LotacaoAdapter implements LotacaoPort {

    private final LotacaoServicePort lotacaoService;

    @Override
    public void atualizarLotacao(LotacaoJpa lotacao, LotacaoJSarh lotacaoJSarh) {
        lotacaoService.atualizarLotacao(lotacao, lotacaoJSarh);
    }

    @Override
    public Set<Integer> getLotacoes(Integer idLotacaoPai) {
        return lotacaoService.getLotacaos(idLotacaoPai);
    }
}