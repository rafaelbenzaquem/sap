package br.jus.trf1.sipe.ponto;

import br.jus.trf1.sipe.externo.jsarh.ausencias.Ausencia;
import br.jus.trf1.sipe.externo.jsarh.ausencias.AusenciasService;
import br.jus.trf1.sipe.externo.jsarh.feriado.FeriadoService;
import br.jus.trf1.sipe.externo.jsarh.feriado.dto.FeriadoResponse;
import br.jus.trf1.sipe.ponto.exceptions.PontoExistenteException;
import br.jus.trf1.sipe.ponto.exceptions.PontoInexistenteException;
import br.jus.trf1.sipe.registro.RegistroService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

import static br.jus.trf1.sipe.comum.util.DataTempoUtil.*;

@Slf4j
@Service
public class PontoService {

    private final PontoRepository pontoRepository;
    private final RegistroService registroService;
    private final AusenciasService ausenciaService;
    private final FeriadoService feriadoService;

    public PontoService(PontoRepository pontoRepository,
                        RegistroService registroService,
                        AusenciasService ausenciaService,

                        FeriadoService feriadoService) {
        this.pontoRepository = pontoRepository;
        this.registroService = registroService;
        this.ausenciaService = ausenciaService;
        this.feriadoService = feriadoService;
    }

    public boolean existe(String matricula, LocalDate dia) {
        return pontoRepository.existsById(PontoId.builder().
                matricula(matricula).
                dia(dia).
                build());
    }

    public Ponto buscaPonto(String matricula, LocalDate dia) {
        log.info("Buscando Ponto - {} - {} ", paraString(dia), matricula);
        var pontoOpt = pontoRepository.findById(PontoId.builder().
                matricula(matricula).
                dia(dia).
                build()
        );
        return pontoOpt.orElseThrow(
                () -> new PontoInexistenteException(matricula, dia)
        );
    }


    public List<Ponto> buscarPontos(String matricula, LocalDate inicio, LocalDate fim) {
        log.info("Lista de Pontos - {} - {} - {} ", paraString(inicio), paraString(fim), matricula);
        return pontoRepository.buscaPontosPorPeriodo(matricula, inicio, fim);
    }

    private String defineDescricao(String descricao, LocalDate dia, Optional<Ausencia> ausencia, Optional<FeriadoResponse> feriado) {
        return descricao + "\n" +
                dia.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.of("pt", "BR")) + "\n" +
                ausencia.map(a -> ", " + a.getDescricao()).orElse("") + "\n" +
                feriado.map(f -> ", " + f.getDescricao()).orElse("");
    }

    private IndicePonto defineIndice(LocalDate dia, Optional<Ausencia> ausencia, Optional<FeriadoResponse> feriado) {
        return ausencia.map(a -> IndicePonto.AUSENCIA).
                orElseGet(() -> feriado.map(f -> IndicePonto.DOMINGO_E_FERIADOS).
                        orElse(dia.getDayOfWeek().getValue() == 7 ? IndicePonto.DOMINGO_E_FERIADOS :
                                dia.getDayOfWeek().getValue() == 6 ? IndicePonto.SABADO :
                                        IndicePonto.DIA_UTIL)
                );
    }

    @Transactional
    public Ponto criaPonto(Ponto ponto) {
        var matricula = ponto.getId().getMatricula();
        var dia = ponto.getId().getDia();
        var descricao = ponto.getDescricao() == null ? "" : "\n" + ponto.getDescricao();
        log.info("Salvando Ponto - {} - {} ", matricula, dia);
        if (this.existe(matricula, dia)) {
            throw new PontoExistenteException(matricula, dia);
        }
        var ausencia = ausenciaService.buscaAusenciaServidorNoDia(matricula, dia);
        var feriadoResponse = feriadoService.buscaFeriadoDoDia(dia);
        descricao = defineDescricao(descricao, dia, ausencia, feriadoResponse);
        var indice = defineIndice(dia, ausencia, feriadoResponse);
        ponto.setDescricao(descricao);
        ponto.setIndice(indice);

        var pontoSalvo = pontoRepository.save(ponto);
        registroService.atualizaRegistrosNovos(pontoSalvo);
        return pontoSalvo;

    }


    @Transactional
    public Ponto atualizaPonto(Ponto ponto) {
        var matricula = ponto.getId().getMatricula();
        var dia = ponto.getId().getDia();
        var descricao = ponto.getDescricao() == null ? "" : ponto.getDescricao();
        log.info("Atualizando Ponto - {} - {} ", matricula, dia);
        if (this.existe(matricula, dia)) {
            var ausencia = ausenciaService.buscaAusenciaServidorNoDia(matricula, dia);
            var feriadoResponse = feriadoService.buscaFeriadoDoDia(dia);
            descricao = defineDescricao(descricao, dia, ausencia, feriadoResponse);
            var indice = defineIndice(dia, ausencia, feriadoResponse);
            ponto.setDescricao(descricao);
            ponto.setIndice(indice);
            registroService.atualizaRegistrosNovos(ponto);
            return pontoRepository.save(ponto);
        }
        throw new PontoInexistenteException(matricula, dia);
    }

    @Transactional
    public List<Ponto> carregaPontos(String matricula, LocalDate inicio, LocalDate fim) {
        List<Ponto> pontos = new ArrayList<>();
        LocalDate dataAtual = inicio;
        while (!dataAtual.isAfter(fim)) {
            var id = PontoId.builder().
                    dia(dataAtual).
                    matricula(matricula).
                    build();

            Optional<Ponto> pontoOpt = pontoRepository.findById(id);
            if (pontoOpt.isPresent()) {
                var ponto = pontoOpt.get();
                var registros = registroService.atualizaRegistrosNovos(ponto);
                ponto.setRegistros(registros);
                pontos.add(ponto);
                dataAtual = dataAtual.plusDays(1);
                continue;
            }
            var ponto = Ponto.builder().id(id).build();
            pontos.add(criaPonto(ponto));
            dataAtual = dataAtual.plusDays(1); // Avança para o próximo dia
        }
        return pontos;
    }
}
