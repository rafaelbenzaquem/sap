package br.jus.trf1.sap.ponto;

import br.jus.trf1.sap.externo.coletor.historico.HistoricoService;
import br.jus.trf1.sap.externo.coletor.historico.dto.HistoricoResponse;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CriaPontosDiariosTask {


    private final VinculoRepository vinculoRepository;
    private final PontoService pontoService;
    private final HistoricoService historicoService;

    public CriaPontosDiariosTask(VinculoRepository vinculoRepository, PontoService pontoService, HistoricoService historicoService) {
        this.vinculoRepository = vinculoRepository;
        this.pontoService = pontoService;
        this.historicoService = historicoService;
    }

    @Scheduled(cron = "00 00 00 * * ? ")
    public void criaPontosDoDia() {
        vinculoRepository.findAll().forEach(v -> {
            var historicos = historicoService.buscarHistoricoDeAcesso(
                    LocalDate.now(), null, v.getCracha(), null, null);
            pontoService.salvaPontoDeHoje(v.getMatricula(), historicos.stream().map(HistoricoResponse::toModel).toList());
        });
    }


}
