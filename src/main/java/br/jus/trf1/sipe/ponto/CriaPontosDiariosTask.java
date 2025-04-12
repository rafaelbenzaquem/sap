package br.jus.trf1.sipe.ponto;

import br.jus.trf1.sipe.externo.coletor.historico.HistoricoService;
import br.jus.trf1.sipe.externo.coletor.historico.dto.HistoricoResponse;
import br.jus.trf1.sipe.usuario.UsuarioRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CriaPontosDiariosTask {


    private final UsuarioRepository usuarioRepository;
    private final PontoService pontoService;
    private final HistoricoService historicoService;

    public CriaPontosDiariosTask(UsuarioRepository usuarioRepository, PontoService pontoService, HistoricoService historicoService) {
        this.usuarioRepository = usuarioRepository;
        this.pontoService = pontoService;
        this.historicoService = historicoService;
    }

    @Scheduled(cron = "00 00 00 * * ? ")
    public void criaPontosDoDia() {
        usuarioRepository.findAll().forEach(v -> {
            var historicos = historicoService.buscarHistoricoDeAcesso(
                    LocalDate.now(), null, v.getCracha(), null, null);
            pontoService.salvaPontoDeHoje(v.getMatricula(), historicos.stream().map(HistoricoResponse::toModel).toList());
        });
    }
}
