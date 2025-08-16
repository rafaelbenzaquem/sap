package br.jus.trf1.sipe.externo.jsarh.lotacao;

import br.jus.trf1.sipe.externo.jsarh.lotacao.dto.LotacaoExternaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
@FeignClient(url = "${jsarh.api.url}", name = "lotacao", fallback = LotacaoExternaClientFallBackImpl.class)
public interface LotacaoExternoClient {

    @GetMapping(value = "/v1/sarh/lotacoes/{id}", produces = "application/json")
    Optional<LotacaoExternaResponse> buscaLotacao(@PathVariable("id") Integer idLotacao);

    @GetMapping(value = "/v1/sarh/lotacoes", produces = "application/json")
    List<LotacaoExternaResponse> lista();

    @GetMapping(value = "/v1/sarh/lotacoes/{idLotacaoPai}/sublotacoes", produces = "application/json")
    List<LotacaoExternaResponse> listaSublotacoes(@PathVariable("idLotacaoPai") Integer idLotacaoPai);
}
