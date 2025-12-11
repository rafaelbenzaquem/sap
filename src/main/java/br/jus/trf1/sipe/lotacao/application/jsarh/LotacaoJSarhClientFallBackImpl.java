package br.jus.trf1.sipe.lotacao.application.jsarh;

import br.jus.trf1.sipe.lotacao.application.jsarh.dto.LotacaoJSarhResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LotacaoJSarhClientFallBackImpl implements LotacaoJSarhClient {

    @Override
    public Optional<LotacaoJSarhResponse> buscaLotacao(Integer idLotacao) {
        return Optional.empty();
    }

    @Override
    public List<LotacaoJSarhResponse> lista() {
        return List.of();
    }

    @Override
    public List<LotacaoJSarhResponse> listaSublotacoes(Integer idLotacaoPai) {
        return List.of();
    }
}
