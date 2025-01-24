package br.jus.trf1.sap.historico;

import br.jus.trf1.sap.historico.dto.HistoricoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@FeignClient(url = "${ponto.coletor.url}", name = "historico")
public interface HistoricoService {


    @GetMapping(value = "/v1/coletor/acessos", produces = "application/json")
    List<HistoricoResponse> buscarHistoricoDeAcesso(@RequestParam("data") String data,
                                                    @RequestParam(name = "cracha", required = false) String cracha,
                                                    @RequestParam(name = "nome", required = false) String nome,
                                                    @RequestParam(name = "codigo", required = false) Integer codigo);
}
