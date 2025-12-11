package br.jus.trf1.sipe.servidor.application.jsarh;

import br.jus.trf1.sipe.lotacao.application.jsarh.LotacaoJSarhClient;
import br.jus.trf1.sipe.lotacao.application.jsarh.exceptions.LotacaoJSarhInexistenteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ServidorJSarhService {

    private final ServidorJSarhClient servidorExternoClient;
    private final LotacaoJSarhClient lotacaoExternoClient;

    public ServidorJSarhService(ServidorJSarhClient servidorExternoClient, LotacaoJSarhClient lotacaoExternoClient) {
        this.servidorExternoClient = servidorExternoClient;
        this.lotacaoExternoClient = lotacaoExternoClient;
    }

    public Optional<ServidorJSarh> buscaServidorExterno(String matricula) {
        log.info("Buscando dados servidor no SARH: {}", matricula);
        var optServidor = servidorExternoClient.buscaDadosServidor(matricula);
        if (optServidor.isPresent()) {
            var servidorExternoResponse = optServidor.get();
            var idLotacao = servidorExternoResponse.getIdLotacao();
            var optLotacaoExternaResponse = lotacaoExternoClient.buscaLotacao(idLotacao);
            if (optLotacaoExternaResponse.isPresent()) {
                return Optional.of(ServidorJSarh.from(servidorExternoResponse, optLotacaoExternaResponse.get()));
            }
            throw new LotacaoJSarhInexistenteException("Lotacao id:" + idLotacao + " inexistente.");
        }
        return Optional.empty(); // throw new ServidorExternoInexistenteException("Servidor '%s' inexistente".formatted(matricula));
    }

}
