package br.jus.trf1.sipe.folha;

import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.servidor.ServidorService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

@Service
public class FolhaService {

    private final FolhaRepository folhaRepository;
    private final PontoService pontoService;
    private final ServidorService servidorService;

    public FolhaService(FolhaRepository folhaRepository,
                        PontoService pontoService,
                        ServidorService servidorService) {
        this.folhaRepository = folhaRepository;
        this.pontoService = pontoService;
        this.servidorService = servidorService;
    }

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

        return folhaRepository.save(folha);
    }

    public Optional<Folha> buscarFolha(String matricula, Mes mes, int ano) {
        return folhaRepository.buscarFolha(matricula, mes.getValor(), ano);
    }

}
