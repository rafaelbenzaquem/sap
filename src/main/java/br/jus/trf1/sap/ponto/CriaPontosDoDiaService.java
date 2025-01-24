package br.jus.trf1.sap.ponto;

import br.jus.trf1.sap.historico.HistoricoService;
import br.jus.trf1.sap.ponto.registro.Registro;
import br.jus.trf1.sap.vinculo.Vinculo;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import static br.jus.trf1.sap.util.DateTimeUtils.*;

@Component
public class CriaPontosDoDiaService {

    private HistoricoService historicoService;


    private VinculoRepository vinculoRepository;


    private PontoService pontoService;

    public CriaPontosDoDiaService(HistoricoService historicoService, VinculoRepository vinculoRepository, PontoService pontoService) {
        this.historicoService = historicoService;
        this.vinculoRepository = vinculoRepository;
        this.pontoService = pontoService;
    }

    @Scheduled(cron = "0 55 22 * * ? ")
    public void criaPontosDoDia() {
        vinculoRepository.findAll().forEach(this::salvarPontoDeUmVinculo);
    }

    @Transactional
    public void salvarPontoDeUmVinculo(Vinculo vinculo) {
        var historicos = historicoService.buscarHistoricoDeAcesso(
                formatarParaString(LocalDate.now())
                , vinculo.getCracha(), null, null);
        var ponto = pontoService.salvarPonto(
                Ponto.builder()
                        .id(
                                PontoId.builder()
                                        .dia(LocalDate.now().minusDays(2))
                                        .matricula(vinculo.getMatricula())
                                        .build())
                        .descricao(
                                LocalDate.now().
                                        minusDays(2).
                                        getDayOfWeek().
                                        getDisplayName(TextStyle.FULL, Locale.of("pt", "BR"))
                        ).
                        build());
        var registros = historicos.stream().map(hr -> Registro.builder()
                        .codigoAcesso(hr.acesso())
                        .hora(hr.dataHora().toLocalTime())
                        .sentido(hr.sentido())
                        .versao(1)
                        .ponto(ponto)
                        .build())
                .toList();
        registros.forEach(pontoService::criarNovoRegistro);
    }


}
