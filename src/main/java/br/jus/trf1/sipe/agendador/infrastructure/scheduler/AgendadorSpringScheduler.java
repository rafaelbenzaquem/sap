package br.jus.trf1.sipe.agendador.infrastructure.scheduler;

import br.jus.trf1.sipe.agendador.domain.port.in.AgendadorServicePort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Adaptador de infraestrutura que utiliza o Spring Scheduler
 * para executar as tarefas agendadas definidas na porta de entrada.
 */
@Component
public class AgendadorSpringScheduler {

    private final AgendadorServicePort agendadorService;

    public AgendadorSpringScheduler(AgendadorServicePort agendadorService) {
        this.agendadorService = agendadorService;
    }

    /**
     * Executa a cada 10 minutos, todos os dias.
     * Cron: segundo minuto hora dia mês diaDaSemana
     * "0 0/10 * * * *" = a cada 10 minutos
     */
    @Scheduled(cron = "0 0/10 * * * *")
    public void agendarTarefaACadaDezMinutos() {
        agendadorService.executarTarefaACadaDezMinutos();
    }

    /**
     * Executa uma vez por dia às 00:00 horas.
     * Cron: "0 0 0 * * *" = às 00:00:00 todos os dias
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void agendarTarefaDiaria() {
        agendadorService.executarTarefaDiaria();
    }

    /**
     * Executa uma vez por semana, no domingo às 00:00 horas.
     * Cron: "0 0 0 * * SUN" = às 00:00:00 todo domingo
     */
    @Scheduled(cron = "0 0 0 * * SUN")
    public void agendarTarefaSemanal() {
        agendadorService.executarTarefaSemanal();
    }
}
