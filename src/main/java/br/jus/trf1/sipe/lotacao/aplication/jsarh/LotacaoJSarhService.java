package br.jus.trf1.sipe.lotacao.aplication.jsarh;

import br.jus.trf1.sipe.lotacao.LotacaoMapping;
import br.jus.trf1.sipe.lotacao.aplication.jsarh.dto.LotacaoJSarhResponse;
import br.jus.trf1.sipe.lotacao.aplication.jsarh.exceptions.LotacaoJSarhInexistenteException;
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
            return LotacaoMapping.toJSarhDomain(opt.get());
        }
        throw new LotacaoJSarhInexistenteException("Lotacao id '%d' inexistente".formatted(idLotacao));
    }


    public List<LotacaoJSarh> listaSublotacoes(Integer idLotacaoPai) {
        log.info("Buscando sublotações de lotacao pai: {}", idLotacaoPai);
        return lotacaoExternoClient.listaSublotacoes(idLotacaoPai).stream().map(LotacaoMapping::toJSarhDomain).toList();
    }

    public List<LotacaoJSarh> lista() {
        log.info("Buscando todas as lotacoes");
        return lotacaoExternoClient.lista().stream().map(LotacaoMapping::toJSarhDomain).toList();
    }

}
