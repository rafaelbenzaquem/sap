package br.jus.trf1.sap.ponto;

import br.jus.trf1.sap.comum.util.DataTempoUtil;
import br.jus.trf1.sap.externo.coletor.historico.HistoricoService;
import br.jus.trf1.sap.externo.coletor.historico.dto.HistoricoResponse;
import br.jus.trf1.sap.externo.jsarh.ausencias.Ausencia;
import br.jus.trf1.sap.externo.jsarh.ausencias.AusenciasService;
import br.jus.trf1.sap.externo.jsarh.feriado.FeriadoService;
import br.jus.trf1.sap.externo.jsarh.feriado.dto.FeriadoResponse;
import br.jus.trf1.sap.ponto.exceptions.PontoInexistenteException;
import br.jus.trf1.sap.registro.Registro;
import br.jus.trf1.sap.vinculo.Vinculo;
import br.jus.trf1.sap.vinculo.VinculoService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Slf4j
@Service
public class PontoService {

    private final PontoRepository pontoRepository;
    private final VinculoService vinculoService;
    private final AusenciasService ausenciaService;
    private final FeriadoService feriadoService;

    public PontoService(PontoRepository pontoRepository, VinculoService vinculoService,
                        AusenciasService ausenciaService, FeriadoService feriadoService) {
        this.pontoRepository = pontoRepository;
        this.vinculoService = vinculoService;
        this.ausenciaService = ausenciaService;
        this.feriadoService = feriadoService;
    }

    public Optional<Ponto> buscaPonto(String matricula, LocalDate dia) {
        log.info("Buscando Ponto - {} - {} ", DataTempoUtil.paraString(dia), matricula);
        return pontoRepository.findById(PontoId.builder().
                matricula(matricula).
                dia(dia).
                build()
        );
    }

    public Ponto atualizaRegistrosPonto(String matricula,
                                                  LocalDate dia,
                                                  List<HistoricoResponse> historicos) {
        log.info("Buscar Atualizacao Ponto - {} - {} ", DataTempoUtil.paraString(dia), matricula);
        var optPonto = pontoRepository.buscaPonto(matricula, dia);
        log.info("ponto {}", optPonto.isPresent() ? "foi encontrato!" : "não foi encontrato!");
        if (optPonto.isPresent()) {
            var ausencia = ausenciaService.buscaAusenciaServidorNoDia(matricula, dia);
            var feriadoResponse = feriadoService.buscaFeriadoDoDia(dia);
            var descricao = defineDescricao(dia, ausencia, feriadoResponse);
            var indice = defineIndice(dia, ausencia, feriadoResponse);
            var ponto = optPonto.get();
            log.info("ponto {}", ponto);
            var vinculo = vinculoService.buscaPorMatricula(matricula);
            log.info("vinculo {}", vinculo);
            var registros = historicos.stream().
                    filter(historico ->
                            {
                                log.debug("historico {}", historico);
                                return ponto.getRegistros().stream().noneMatch(r ->
                                        {
                                            var filtered = Objects.equals(historico.acesso(), r.getCodigoAcesso());
                                            log.debug("registro {} - {}",
                                                    !filtered ? "foi filtrado" : "não foi filtrado", r);
                                            return filtered;
                                        }
                                );
                            }

                    )
                    .map(hr ->
                            Registro.builder()
                                    .codigoAcesso(hr.acesso())
                                    .hora(hr.dataHora().toLocalTime())
                                    .sentido(hr.sentido())
                                    .versao(1)
                                    .ponto(optPonto.get())
                                    .build()
                    )
                    .toList();
            ponto.setIndice(indice);
            ponto.setDescricao(descricao);
            ponto.getRegistros().addAll(registros);
           return pontoRepository.save(ponto);
        }

        throw new PontoInexistenteException(matricula, dia);
    }


    public List<Ponto> buscarPontos(String matricula, LocalDate inicio, LocalDate fim) {
        log.info("Lista de Pontos - {} - {} - {} ", DataTempoUtil.paraString(inicio), DataTempoUtil.paraString(fim), matricula);
        return pontoRepository.buscaPontosPorPeriodo(matricula, inicio, fim);
    }

    public Ponto salvaPonto(Ponto ponto) {
        log.info("Salvando Ponto - {} - {} ", DataTempoUtil.paraString(ponto.getId().getDia()), ponto.getId().getMatricula());
        return pontoRepository.save(ponto);
    }


    private String defineDescricao(LocalDate dia, Optional<Ausencia> ausencia, Optional<FeriadoResponse> feriado) {
        var descricao = dia.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.of("pt", "BR"));
        descricao += ausencia.map(a -> ", " + a.getDescricao()).orElse("");
        descricao += feriado.map(f -> ", " + f.getDescricao()).orElse("");
        return descricao;
    }

    private IndicePonto defineIndice(LocalDate dia, Optional<Ausencia> ausencia, Optional<FeriadoResponse> feriado) {
        var indicePonto = ausencia.map(a -> IndicePonto.AUSENCIA).
                orElseGet(() -> feriado.map(f -> IndicePonto.DOMINGO_E_FERIADOS).
                        orElse(dia.getDayOfWeek().getValue() == 7 ? IndicePonto.DOMINGO_E_FERIADOS :
                                dia.getDayOfWeek().getValue() == 6 ? IndicePonto.SABADO :
                                        IndicePonto.DIA_UTIL)
                );
        return indicePonto;
    }

    @Transactional
    public Ponto salvaPonto(String matricula, LocalDate dia, List<Registro> registros) {
        var ausencia = ausenciaService.buscaAusenciaServidorNoDia(matricula, dia);
        var feriadoResponse = feriadoService.buscaFeriadoDoDia(dia);
        var descricao = defineDescricao(dia, ausencia, feriadoResponse);
        var indice = defineIndice(dia, ausencia, feriadoResponse);

        var ponto = pontoRepository.save(
                Ponto.builder().
                        id(PontoId.builder().
                                dia(dia).
                                matricula(matricula).
                                build()).
                        descricao(descricao).
                        indice(indice.getValor()).
                        build());

        if (registros == null || registros.isEmpty()) {
            return ponto;
        }
        registros.forEach(registro -> registro.setPonto(ponto));
        if (ponto.getRegistros() == null) ponto.setRegistros(new ArrayList<>(registros));
        else ponto.getRegistros().addAll(new ArrayList<>(registros));

        return pontoRepository.save(ponto);
    }

    @Transactional
    public List<Ponto> carregaPontos(Vinculo vinculo, LocalDate inicio, LocalDate fim,
                                     HistoricoService historicoService) {
        List<Ponto> pontos = new ArrayList<>();
        LocalDate dataAtual = inicio;
        while (!dataAtual.isAfter(fim)) {
            var matricula = vinculo.getMatricula();
            var id = PontoId.builder().
                    dia(dataAtual).
                    matricula(matricula).
                    build();

            Optional<Ponto> pontoOpt = pontoRepository.findById(id);

            var historicos = historicoService.buscarHistoricoDeAcesso(
                    dataAtual, null, vinculo.getCracha(), null, null
            );
            var registros = historicos.stream().map(HistoricoResponse::toModel).toList();

            if (pontoOpt.isPresent()) {
                var ausencia = ausenciaService.buscaAusenciaServidorNoDia(matricula, dataAtual);
                var feriadoResponse = feriadoService.buscaFeriadoDoDia(dataAtual);
                var descricao = defineDescricao(dataAtual, ausencia, feriadoResponse);
                var indice = defineIndice(dataAtual, ausencia, feriadoResponse);
                var registrosFiltrados = pontoOpt.get().getRegistros().stream().filter(
                        r -> registros.stream().map(Registro::getHora).toList().contains(r.getHora())).toList();
                var ponto = pontoOpt.get();
                ponto.setIndice(indice);
                ponto.setDescricao(descricao);
                ponto.getRegistros().addAll(new ArrayList<>(registrosFiltrados));
                ponto = salvaPonto(ponto);
                pontos.add(ponto);
                dataAtual = dataAtual.plusDays(1); // Avança para o próximo dia
                continue;
            }

            pontos.add(
                    salvaPonto(vinculo.getMatricula(), dataAtual, registros)
            );
            dataAtual = dataAtual.plusDays(1); // Avança para o próximo dia
        }
        return pontos;
    }

    @Transactional
    public void salvaPontoDeHoje(String matricula, List<Registro> registros) {
        salvaPonto(matricula, LocalDate.now(), registros);
    }

    public Ponto adicionaRegistros(String matricula, LocalDate dia, List<Registro> registros) {
        Ponto ponto = pontoRepository.buscaPonto(matricula, dia).
                orElseThrow(() -> new PontoInexistenteException(matricula, dia));
        ponto.getRegistros().addAll(registros);
        return pontoRepository.save(ponto);
    }
}
