package br.jus.trf1.sipe.registro.infrastructure.coletor;

import br.jus.trf1.sipe.registro.infrastructure.coletor.dto.RegistroColetorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;

@Service
@FeignClient(url = "${coletor.api.url}", fallback = RegistroColetorClientFallBackImpl.class, name = "registro")
public interface RegistroColetorClient {


    @GetMapping(value = "/v1/coletor/acessos", produces = "application/json")
    List<RegistroColetorResponse> buscarRegistrosDeAcesso(@RequestParam(name = "inicio", required = false)
                                                   @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                   LocalDate inicio,
                                                          @RequestParam(name = "fim", required = false)
                                                   @DateTimeFormat(pattern = PADRAO_ENTRADA_DATA)
                                                   LocalDate fim,
                                                          @RequestParam(name = "cracha", required = false) String cracha,
                                                          @RequestParam(name = "nome", required = false) String nome,
                                                          @RequestParam(name = "codigo", required = false) Integer codigo);
}
