package br.jus.trf1.sipe.fechamento.domain.service;

import br.jus.trf1.sipe.fechamento.domain.model.Fechamento;
import br.jus.trf1.sipe.fechamento.domain.port.in.FechamentoServicePort;
import br.jus.trf1.sipe.fechamento.domain.port.out.FechamentoPersistencePort;
import br.jus.trf1.sipe.folha.domain.port.in.FolhaServicePort;
import br.jus.trf1.sipe.folha.domain.model.Mes;
import br.jus.trf1.sipe.servidor.domain.port.in.ServidorServicePort;
import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * Serviço para fechamento de folha, calculando saldos e persistindo prazos.
 */
@Service
public class FechamentoServiceAdapter implements FechamentoServicePort {
    private final FolhaServicePort folhaServicePort;
    private final ServidorServicePort servidorServicePort;
    private final FechamentoPersistencePort fechamentoPersistencePort;

    public FechamentoServiceAdapter(FolhaServicePort folhaServicePort,
                                    ServidorServicePort servidorServicePort,
                                    FechamentoPersistencePort fechamentoPersistencePort) {
        this.folhaServicePort = folhaServicePort;
        this.servidorServicePort = servidorServicePort;
        this.fechamentoPersistencePort = fechamentoPersistencePort;
    }

    /**
     * Executa o fechamento de folha para um servidor em determinado mês/ano.
     * Calcula horas executadas, horas esperadas e saldo, e define prazo de compensação (3 meses).
     * @throws IllegalStateException se já existir fechamento para o período.
     */
    @Override
    public Fechamento fecharFolha(String matricula, Integer valorMes, Integer ano) {
        // valida se já foi fechado
        fechamentoPersistencePort.buscaPorMatriculaMesAno(matricula, valorMes, ano)
                .ifPresent(f -> { throw new IllegalStateException(
                    "FolhaJpa já fechada para " + matricula + " em " + valorMes + "/" + ano);
                });
        // obtém servidor e folha
        Servidor servidor = servidorServicePort.buscaPorMatricula(matricula);
        var mesEnum = Mes.getMes(valorMes);
        var folha = folhaServicePort.buscarFolha(matricula, mesEnum, ano)
                .orElseGet(() -> folhaServicePort.abrirFolha(matricula, mesEnum, ano));
        // soma horas executadas
        long totalMinutos = folha.getPontos().stream()
                .mapToLong(p -> p.getHorasPermanencia().toMinutes())
                .sum();
        // calcula dias uteis no mes
        YearMonth ym = YearMonth.of(ano, valorMes);
        int diasUteis = 0;
        for (int d = 1; d <= ym.lengthOfMonth(); d++) {
            LocalDate data = LocalDate.of(ano, valorMes, d);
            DayOfWeek dw = data.getDayOfWeek();
            if (dw != DayOfWeek.SATURDAY && dw != DayOfWeek.SUNDAY) {
                diasUteis++;
            }
        }
        // horas esperadas em minutos
        long esperados = (long) servidor.getHoraDiaria() * diasUteis * 60L;
        long saldo = totalMinutos - esperados;
        // datas de fechamento e prazo
        Timestamp fechamento = Timestamp.valueOf(LocalDateTime.now());
        Timestamp prazo = Timestamp.valueOf(LocalDateTime.now().plusMonths(3));
        // persiste
        Fechamento fechamentoJpaFolha = Fechamento.builder()
                .servidor(servidor)
                .mes(valorMes)
                .ano(ano)
                .horasExecutadasMinutos(totalMinutos)
                .horasEsperadasMinutos(esperados)
                .saldoMinutos(saldo)
                .dataFechamento(fechamento)
                .prazoCompensacao(prazo)
                .build();
        return fechamentoPersistencePort.salva(fechamentoJpaFolha);
    }
}