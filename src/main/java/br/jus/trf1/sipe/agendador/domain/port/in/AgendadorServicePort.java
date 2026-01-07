package br.jus.trf1.sipe.agendador.domain.port.in;

/**
 * Porta de entrada que define os contratos para as tarefas agendadas.
 * Esta interface define os métodos que serão executados em intervalos específicos.
 */
public interface AgendadorServicePort {

    /**
     * Tarefa executada a cada 10 minutos, todos os dias.
     */
    void executarTarefaACadaDezMinutos();

    /**
     * Tarefa executada uma vez por dia às 00:00 horas.
     */
    void executarTarefaDiaria();

    /**
     * Tarefa executada uma vez por semana, no domingo às 00:00 horas.
     */
    void executarTarefaSemanal();
}
