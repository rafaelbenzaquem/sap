package br.jus.trf1.sipe.agendador.domain.service;

import br.jus.trf1.sipe.agendador.domain.port.in.AgendadorServicePort;
import br.jus.trf1.sipe.ausencia.ausencia.domain.port.in.AusenciaServicePort;
import br.jus.trf1.sipe.feriado.domain.port.in.FeriadoServicePort;
import br.jus.trf1.sipe.servidor.domain.port.in.ServidorServicePort;
import br.jus.trf1.sipe.usuario.domain.port.in.UsuarioServicePort;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementação do serviço de agendamento.
 * Contém a lógica de negócio para as tarefas agendadas.
 */
@Slf4j
@Service
public class AgendadorServiceAdapter implements AgendadorServicePort {


    private final ServidorServicePort servidorServicePort;
    private final AusenciaServicePort ausenciaServicePort;
    private final FeriadoServicePort feriadoServicePort;


    public AgendadorServiceAdapter(ServidorServicePort servidorServicePort,
                                   AusenciaServicePort ausenciaServicePort,
                                   FeriadoServicePort feriadoServicePort) {
        this.servidorServicePort = servidorServicePort;
        this.ausenciaServicePort = ausenciaServicePort;
        this.feriadoServicePort = feriadoServicePort;
    }

    @Override
    public void executarTarefaACadaDezMinutos() {
        log.info("Executando tarefa a cada 10 minutos - {}", LocalDateTime.now());
        // TODO: Implementar a lógica de negócio da tarefa que executa a cada 10 minutos
    }

    @Override
    public void executarTarefaDiaria() {
        log.info("Executando tarefa diária às 00:00 - {}", LocalDateTime.now());
        // TODO: Implementar a lógica de negócio da tarefa diária
    }

    @Override
    public void executarTarefaSemanal() {
        log.info("Executando tarefa semanal (Domingo às 00:00) - {}", LocalDateTime.now());
        // TODO: Implementar a lógica de negócio da tarefa semanal
    }
}
