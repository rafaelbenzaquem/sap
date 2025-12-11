package br.jus.trf1.sipe.lotacao.application.jsarh;

import br.jus.trf1.sipe.lotacao.application.jsarh.dto.LotacaoJSarhResponse;
import br.jus.trf1.sipe.lotacao.application.jsarh.exceptions.LotacaoJSarhInexistenteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LotacaoJSarhService {

    private final LotacaoJSarhClient lotacaoExternoClient;

    public LotacaoJSarhService(LotacaoJSarhClient lotacaoExternoClient) {
        this.lotacaoExternoClient = lotacaoExternoClient;
    }

    public LotacaoJSarh buscaLotacao(Integer idLotacao) {
        log.info("Buscando dados lotacao no SARH: {}", idLotacao);
        Optional<LotacaoJSarhResponse> opt = lotacaoExternoClient.buscaLotacao(idLotacao);
        if (opt.isPresent()) {
            return LotacaoJSarhMapper.toJSarhModel(opt.get());
        }
        throw new LotacaoJSarhInexistenteException("Lotacao id '%d' inexistente".formatted(idLotacao));
    }


    public List<LotacaoJSarh> listaSublotacoes(Integer idLotacaoPai) {
        log.info("Buscando sublotações de lotacao pai: {}", idLotacaoPai);
        return lotacaoExternoClient.listaSublotacoes(idLotacaoPai).stream().map(LotacaoJSarhMapper::toJSarhModel).toList();
    }

    public List<LotacaoJSarh> lista() {
        log.info("Buscando todas as lotacoes");
        return lotacaoExternoClient.lista().stream().map(LotacaoJSarhMapper::toJSarhModel).toList();
    }

}
