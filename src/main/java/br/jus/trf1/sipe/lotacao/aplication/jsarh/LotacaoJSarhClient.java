package br.jus.trf1.sipe.lotacao.aplication.jsarh;

import br.jus.trf1.sipe.lotacao.aplication.jsarh.dto.LotacaoJSarhResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
@FeignClient(url = "${jsarh.api.url}", name = "lotacao", fallback = LotacaoJSarhClientFallBackImpl.class)
public interface LotacaoJSarhClient {

    @GetMapping(value = "/v1/sarh/lotacoes/{id}", produces = "application/json")
    Optional<LotacaoJSarhResponse> buscaLotacao(@PathVariable("id") Integer idLotacao);

    @GetMapping(value = "/v1/sarh/lotacoes", produces = "application/json")
    List<LotacaoJSarhResponse> lista();

    @GetMapping(value = "/v1/sarh/lotacoes/{idLotacaoPai}/sublotacoes", produces = "application/json")
    List<LotacaoJSarhResponse> listaSublotacoes(@PathVariable("idLotacaoPai") Integer idLotacaoPai);
}
