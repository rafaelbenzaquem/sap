package br.jus.trf1.sipe.folha;

import br.jus.trf1.sipe.servidor.infrastructure.persistence.ServidorJpa;
import br.jus.trf1.sipe.servidor.ServidorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * Serviço para fechamento de folha, calculando saldos e persistindo prazos.
 */
@Service
public class FechamentoFolhaService {
    private final FolhaService folhaService;
    private final ServidorService servidorService;
    private final FechamentoFolhaRepository repository;

    public FechamentoFolhaService(FolhaService folhaService,
                                  ServidorService servidorService,
                                  FechamentoFolhaRepository repository) {
        this.folhaService = folhaService;
        this.servidorService = servidorService;
        this.repository = repository;
    }

    /**
     * Executa o fechamento de folha para um servidor em determinado mês/ano.
     * Calcula horas executadas, horas esperadas e saldo, e define prazo de compensação (3 meses).
     * @throws IllegalStateException se já existir fechamento para o período.
     */
    @Transactional
    public FechamentoFolha fecharFolha(String matricula, Integer valorMes, Integer ano) {
        // valida se já foi fechado
        repository.findByServidorMatriculaAndMesAndAno(matricula, valorMes, ano)
                .ifPresent(f -> { throw new IllegalStateException(
                    "Folha já fechada para " + matricula + " em " + valorMes + "/" + ano);
                });
        // obtém servidor e folha
        ServidorJpa servidor = servidorService.buscaPorMatricula(matricula);
        var mesEnum = Mes.getMes(valorMes);
        var folha = folhaService.buscarFolha(matricula, mesEnum, ano)
                .orElseGet(() -> folhaService.abrirFolha(matricula, mesEnum, ano));
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
        FechamentoFolha fechamentoFolha = FechamentoFolha.builder()
                .servidor(servidor)
                .mes(valorMes)
                .ano(ano)
                .horasExecutadasMinutos(totalMinutos)
                .horasEsperadasMinutos(esperados)
                .saldoMinutos(saldo)
                .dataFechamento(fechamento)
                .prazoCompensacao(prazo)
                .build();
        return repository.save(fechamentoFolha);
    }
}