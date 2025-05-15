package br.jus.trf1.sipe.servidor;

import br.jus.trf1.sipe.externo.jsarh.ausencias.AusenciasExternalService;
import br.jus.trf1.sipe.externo.jsarh.servidor.ServidorExternoService;
import br.jus.trf1.sipe.ponto.PontoRepository;
import org.springframework.stereotype.Service;

@Service
public class ServidorService {

    private final ServidorExternoService servidorExternoService;
    private final AusenciasExternalService ausenciasExternalService;

    public ServidorService(PontoRepository pontoRepository,
                           ServidorExternoService servidorExternoService,
                           AusenciasExternalService ausenciasExternalService) {
        this.servidorExternoService = servidorExternoService;
        this.ausenciasExternalService = ausenciasExternalService;
    }

    
}
