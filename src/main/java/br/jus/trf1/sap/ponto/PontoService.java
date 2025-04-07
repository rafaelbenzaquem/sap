package br.jus.trf1.sap.ponto;

import br.jus.trf1.sap.externo.coletor.historico.HistoricoService;
import br.jus.trf1.sap.externo.coletor.historico.dto.HistoricoResponse;
import br.jus.trf1.sap.externo.jsarh.ausencias.Ausencia;
import br.jus.trf1.sap.externo.jsarh.ausencias.AusenciasService;
import br.jus.trf1.sap.externo.jsarh.feriado.FeriadoService;
import br.jus.trf1.sap.externo.jsarh.feriado.dto.FeriadoResponse;
import br.jus.trf1.sap.ponto.exceptions.PontoExistenteException;
import br.jus.trf1.sap.ponto.exceptions.PontoNaoEncontradoException;
import br.jus.trf1.sap.registro.Registro;
import br.jus.trf1.sap.usuario.Usuario;
import br.jus.trf1.sap.usuario.UsuarioService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

import static br.jus.trf1.sap.comum.util.DataTempoUtil.*;

@Slf4j
@Service
public class PontoService {

    private final PontoRepository pontoRepository;
    private final UsuarioService usuarioService;
    private final AusenciasService ausenciaService;
    private final FeriadoService feriadoService;

    public PontoService(PontoRepository pontoRepository, UsuarioService usuarioService,
                        AusenciasService ausenciaService, FeriadoService feriadoService) {
        this.pontoRepository = pontoRepository;
        this.usuarioService = usuarioService;
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
                () -> new PontoNaoEncontradoException(matricula,dia)
        );
    }


    public List<Ponto> buscarPontos(String matricula, LocalDate inicio, LocalDate fim) {
        log.info("Lista de Pontos - {} - {} - {} ", paraString(inicio), paraString(fim), matricula);
        return pontoRepository.buscaPontosPorPeriodo(matricula, inicio, fim);
    }

    public Ponto salvaPonto(Ponto ponto) {
        log.info("Salvando Ponto - {} - {} ", paraString(ponto.getId().getDia()), ponto.getId().getMatricula());
        return pontoRepository.save(ponto);
    }


    private String defineDescricao(LocalDate dia, Optional<Ausencia> ausencia, Optional<FeriadoResponse> feriado) {
        var descricao = dia.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.of("pt", "BR"));
        descricao += ausencia.map(a -> ", " + a.getDescricao()).orElse("");
        descricao += feriado.map(f -> ", " + f.getDescricao()).orElse("");
        return descricao;
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
    public Ponto salvaPonto(String matricula, LocalDate dia, List<Registro> registros) {
        if (this.existe(matricula, dia)) {
            throw new PontoExistenteException(matricula, dia);
        }
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
        ponto.setRegistros(new ArrayList<>(registros));

        return pontoRepository.save(ponto);
    }

    @Transactional
    public List<Ponto> carregaPontos(Usuario usuario, LocalDate inicio, LocalDate fim,
                                     HistoricoService historicoService) {
        List<Ponto> pontos = new ArrayList<>();
        LocalDate dataAtual = inicio;
        while (!dataAtual.isAfter(fim)) {
            var matricula = usuario.getMatricula();
            var id = PontoId.builder().
                    dia(dataAtual).
                    matricula(matricula).
                    build();

            Optional<Ponto> pontoOpt = pontoRepository.findById(id);

            var historicos = historicoService.buscarHistoricoDeAcesso(
                    dataAtual, null, usuario.getCracha(), null, null
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
                    salvaPonto(usuario.getMatricula(), dataAtual, registros)
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
                orElseThrow(() -> new PontoNaoEncontradoException(matricula, dia));
        ponto.getRegistros().addAll(registros);
        return pontoRepository.save(ponto);
    }
}
