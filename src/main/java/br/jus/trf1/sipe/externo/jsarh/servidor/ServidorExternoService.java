package br.jus.trf1.sipe.externo.jsarh.servidor;

import br.jus.trf1.sipe.externo.jsarh.lotacao.LotacaoExternoClient;
import br.jus.trf1.sipe.externo.jsarh.lotacao.exceptions.LotacaoExternaInexistenteException;
import br.jus.trf1.sipe.externo.jsarh.servidor.exceptions.ServidorExternoInexistenteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ServidorExternoService {

    private final ServidorExternoClient servidorExternoClient;
    private final LotacaoExternoClient lotacaoExternoClient;

    public ServidorExternoService(ServidorExternoClient servidorExternoClient, LotacaoExternoClient lotacaoExternoClient) {
        this.servidorExternoClient = servidorExternoClient;
        this.lotacaoExternoClient = lotacaoExternoClient;
    }

    public ServidorExterno buscaServidorExterno(String matricula) {
        log.info("Buscando dados servidor no SARH: {}", matricula);
        var optServidor = servidorExternoClient.buscaDadosServidor(matricula);
        if (optServidor.isPresent()) {
            var servidorExternoResponse = optServidor.get();
            var idLotacao = servidorExternoResponse.getIdLotacao();
            var optLotacaoExternaResponse = lotacaoExternoClient.buscaLotacao(idLotacao);
            if (optLotacaoExternaResponse.isPresent()) {
                return ServidorExterno.from(servidorExternoResponse, optLotacaoExternaResponse.get());
            }
            var msg_warning = "Lotacao id:" + idLotacao + " inexistente.";
            log.warn(msg_warning);
            throw new LotacaoExternaInexistenteException(msg_warning);
        }
        var msg_warning = "Servidor '%s' inexistente".formatted(matricula);
        log.warn(msg_warning);
        throw new ServidorExternoInexistenteException(msg_warning);
    }

}
