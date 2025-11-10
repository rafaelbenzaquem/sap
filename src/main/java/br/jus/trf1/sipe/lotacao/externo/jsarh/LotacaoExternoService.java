package br.jus.trf1.sipe.lotacao.externo.jsarh;

import br.jus.trf1.sipe.lotacao.externo.jsarh.dto.LotacaoExternaResponse;
import br.jus.trf1.sipe.lotacao.externo.jsarh.exceptions.LotacaoExternaInexistenteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LotacaoExternoService {

    private final LotacaoExternoClient lotacaoExternoClient;

    public LotacaoExternoService(LotacaoExternoClient lotacaoExternoClient) {
        this.lotacaoExternoClient = lotacaoExternoClient;
    }

    public LotacaoExterna buscaLotacao(Integer idLotacao) {
        log.info("Buscando dados lotacao no SARH: {}", idLotacao);
        Optional<LotacaoExternaResponse> opt = lotacaoExternoClient.buscaLotacao(idLotacao);
        if (opt.isPresent()) {
            return LotacaoExterna.from(opt.get());
        }
        throw new LotacaoExternaInexistenteException("Lotacao id '%d' inexistente".formatted(idLotacao));
    }


    public List<LotacaoExterna> listaSublotacoes(Integer idLotacaoPai) {
        log.info("Buscando sublotações de lotacao pai: {}", idLotacaoPai);
        return lotacaoExternoClient.listaSublotacoes(idLotacaoPai).stream().map(LotacaoExterna::from).toList();
    }

    public List<LotacaoExterna> lista() {
        log.info("Buscando todas as lotacoes");
        return lotacaoExternoClient.lista().stream().map(LotacaoExterna::from).toList();
    }

}
