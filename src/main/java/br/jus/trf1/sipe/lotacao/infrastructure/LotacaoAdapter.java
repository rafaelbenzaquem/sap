package br.jus.trf1.sipe.lotacao.infrastructure;

import br.jus.trf1.sipe.lotacao.application.jsarh.LotacaoJSarh;
import br.jus.trf1.sipe.lotacao.infrastructure.persistence.LotacaoJpa;
import br.jus.trf1.sipe.lotacao.domain.port.in.LotacaoServicePort;
import br.jus.trf1.sipe.lotacao.domain.port.out.LotacaoPort; // A porta que este adaptador implementa
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class LotacaoAdapter implements LotacaoPort {

    private final LotacaoServicePort lotacaoServicePort;

    @Override
    public void atualizarLotacao(LotacaoJpa lotacao, LotacaoJSarh lotacaoJSarh) {
        lotacaoServicePort.atualizarLotacao(lotacao, lotacaoJSarh);
    }

    @Override
    public Set<Integer> getLotacoes(Integer idLotacaoPai) {
        return lotacaoServicePort.getLotacaos(idLotacaoPai);
    }
}