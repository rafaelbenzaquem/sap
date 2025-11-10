package br.jus.trf1.sipe.servidor.externo.jsarh;

import br.jus.trf1.sipe.lotacao.externo.jsarh.LotacaoExternoClient;
import br.jus.trf1.sipe.lotacao.externo.jsarh.exceptions.LotacaoExternaInexistenteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ServidorExternoService {

    private final ServidorExternoClient servidorExternoClient;
    private final LotacaoExternoClient lotacaoExternoClient;

    public ServidorExternoService(ServidorExternoClient servidorExternoClient, LotacaoExternoClient lotacaoExternoClient) {
        this.servidorExternoClient = servidorExternoClient;
        this.lotacaoExternoClient = lotacaoExternoClient;
    }

    public Optional<ServidorExterno> buscaServidorExterno(String matricula) {
        log.info("Buscando dados servidor no SARH: {}", matricula);
        var optServidor = servidorExternoClient.buscaDadosServidor(matricula);
        if (optServidor.isPresent()) {
            var servidorExternoResponse = optServidor.get();
            var idLotacao = servidorExternoResponse.getIdLotacao();
            var optLotacaoExternaResponse = lotacaoExternoClient.buscaLotacao(idLotacao);
            if (optLotacaoExternaResponse.isPresent()) {
                return Optional.of(ServidorExterno.from(servidorExternoResponse, optLotacaoExternaResponse.get()));
            }
            throw new LotacaoExternaInexistenteException("Lotacao id:" + idLotacao + " inexistente.");
        }
        return Optional.empty(); // throw new ServidorExternoInexistenteException("Servidor '%s' inexistente".formatted(matricula));
    }

}
