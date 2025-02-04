package br.jus.trf1.sap.ponto;

import br.jus.trf1.sap.vinculo.VinculoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CriaPontosDiariosTask {


    private final VinculoRepository vinculoRepository;
    private final PontoService pontoService;

    public CriaPontosDiariosTask(VinculoRepository vinculoRepository, PontoService pontoService) {
        this.vinculoRepository = vinculoRepository;
        this.pontoService = pontoService;
    }

    @Scheduled(cron = "00 42 11 * * ? ")
    public void criaPontosDoDia() {
        vinculoRepository.findAll().forEach(pontoService::salvarPontoDeHojeDeUmVinculo);
    }


}
