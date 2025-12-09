package br.jus.trf1.sipe.lotacao.infrastructure;

import br.jus.trf1.sipe.lotacao.domain.model.Lotacao;
import br.jus.trf1.sipe.lotacao.domain.port.in.LotacaoServicePort;
import br.jus.trf1.sipe.lotacao.externo.jsarh.to.LotacaoExternaTO;
import br.jus.trf1.sipe.lotacao.domain.port.out.LotacaoPort; // A porta que este adaptador implementa
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class LotacaoAdapter implements LotacaoPort {

    private final LotacaoServicePort lotacaoService;

    @Override
    public void atualizarLotacao(Lotacao lotacao, LotacaoExternaTO lotacaoExterna) {
        lotacaoService.atualizarLotacao(lotacao, lotacaoExterna);
    }

    @Override
    public Set<Integer> getLotacaos(Integer idLotacaoPai) {
        return lotacaoService.getLotacaos(idLotacaoPai);
    }
}