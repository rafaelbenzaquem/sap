package br.jus.trf1.sipe.folha.domain.service;

import br.jus.trf1.sipe.folha.domain.model.FolhaId;
import br.jus.trf1.sipe.folha.domain.model.Mes;
import br.jus.trf1.sipe.folha.domain.model.Folha;
import br.jus.trf1.sipe.folha.domain.port.in.FolhaServicePort;
import br.jus.trf1.sipe.folha.domain.port.out.FolhaPersistencePort;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.servidor.domain.service.ServidorServiceAdapter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

@Service
public class FolhaServiceAdapter implements FolhaServicePort {

    private final FolhaPersistencePort folhaPersistencePort;
    private final PontoService pontoService;
    private final ServidorServiceAdapter servidorService;

    public FolhaServiceAdapter(FolhaPersistencePort folhaPersistencePort,
                               PontoService pontoService,
                               ServidorServiceAdapter servidorService) {
        this.folhaPersistencePort = folhaPersistencePort;
        this.pontoService = pontoService;
        this.servidorService = servidorService;
    }

    @Override
    public Folha abrirFolha(String matricula, Mes mes, int ano) {
        var servidor = servidorService.buscaPorMatricula(matricula);
        var diaInicial = LocalDate.of(ano, mes.getValor(), 1);
        var diaFinal = YearMonth.of(ano, mes.getValor()).atEndOfMonth();
        var pontos = pontoService.carregaPontos(matricula, diaInicial, diaFinal);
        var folha = Folha.builder()
                .id(FolhaId.builder()
                        .servidor(servidor)
                        .ano(ano)
                        .mes(mes.getValor())
                        .build())
                .pontos(pontos)
                .build();
        return folhaPersistencePort.salva(folha);
    }

    @Override
    public Optional<Folha> buscarFolha(String matricula, Mes mes, int ano) {
        return folhaPersistencePort.buscaFolha(matricula, mes.getValor(), ano);
    }

}
