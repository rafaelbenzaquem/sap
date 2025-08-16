package br.jus.trf1.sipe.externo.jsarh.lotacao;

import br.jus.trf1.sipe.externo.jsarh.lotacao.dto.LotacaoExternaResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LotacaoExternaClientFallBackImpl implements LotacaoExternoClient {

    @Override
    public Optional<LotacaoExternaResponse> buscaLotacao(Integer idLotacao) {
        return Optional.empty();
    }

    @Override
    public List<LotacaoExternaResponse> lista() {
        return List.of();
    }

    @Override
    public List<LotacaoExternaResponse> listaSublotacoes(Integer idLotacaoPai) {
        return List.of();
    }
}
