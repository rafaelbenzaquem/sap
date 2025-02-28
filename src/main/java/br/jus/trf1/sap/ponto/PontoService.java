package br.jus.trf1.sap.ponto;

import br.jus.trf1.sap.externo.coletor.historico.HistoricoService;
import br.jus.trf1.sap.externo.coletor.historico.dto.HistoricoResponse;
import br.jus.trf1.sap.ponto.exceptions.PontoInexistenteException;
import br.jus.trf1.sap.registro.Registro;
import br.jus.trf1.sap.registro.exceptions.RegistroInexistenteException;
import br.jus.trf1.sap.vinculo.Vinculo;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

import static br.jus.trf1.sap.util.DataTempoUtil.dataParaString;

@Slf4j
@Service
public class PontoService {

    private final PontoRepository pontoRepository;
    private final VinculoRepository vinculoRepository;

    public PontoService(PontoRepository pontoRepository, VinculoRepository vinculoRepository) {
        this.pontoRepository = pontoRepository;
        this.vinculoRepository = vinculoRepository;
    }

    public Optional<Ponto> buscaPonto(Integer matricula, LocalDate dia) {
        log.info("Buscando Ponto - {} - {} ", dataParaString(dia), matricula);
        return pontoRepository.findById(PontoId.builder().
                matricula(matricula).
                dia(dia).
                build()
        );
    }

    public Optional<Ponto> atualizaRegistrosPonto(Integer matricula,
                                                  LocalDate dia,
                                                  List<HistoricoResponse> historicos) {
        log.info("Buscar Atualizacao Ponto - {} - {} ", dataParaString(dia), matricula);
        var optPonto = pontoRepository.buscaPonto(matricula, dia);
        log.info("ponto {}", optPonto.isPresent() ? "foi encontrato!" : "não foi encontrato!");
        if (optPonto.isPresent()) {
            Ponto ponto = optPonto.get();
            log.info("ponto {}", ponto);
            var vinculo = vinculoRepository.findVinculoByMatricula(matricula).orElseThrow(RegistroInexistenteException::new);
            log.info("vinculo {}", vinculo);
            var registros = historicos.stream().
                    filter(hr ->
                            {
                                log.debug("historico {}", hr);
                                return ponto.getRegistros().stream().noneMatch(r ->
                                        {
                                            var filtered = Objects.equals(hr.acesso(), r.getCodigoAcesso());
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

            ponto.getRegistros().addAll(registros);
            pontoRepository.save(ponto);
            return Optional.of(ponto);
        }

        return Optional.empty();
    }


    public List<Ponto> buscarPontos(Integer matricula, LocalDate inicio, LocalDate fim) {
        log.info("Lista de Pontos - {} - {} - {} ", dataParaString(inicio), dataParaString(fim), matricula);
        return pontoRepository.buscaPontosPorPeriodo(matricula, inicio, fim);
    }

    public Ponto salvaPonto(Ponto ponto) {
        log.info("Salvando Ponto - {} - {} ", dataParaString(ponto.getId().getDia()), ponto.getId().getMatricula());
        return pontoRepository.save(ponto);
    }


    @Transactional
    public Ponto salvaPontoPorData(Integer matricula, LocalDate data, List<HistoricoResponse> historicos) {
        var ponto = pontoRepository.save(
                Ponto.builder().
                        id(PontoId.builder().
                                dia(data).
                                matricula(matricula).
                                build())
                        .descricao(data.
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

        if(registros.isEmpty()) {
            return ponto;
        }
        if (ponto.getRegistros() == null) ponto.setRegistros(registros);
        else ponto.getRegistros().addAll(registros);
        pontoRepository.save(ponto);
        return ponto;
    }

    @Transactional
    public List<Ponto> carregaPontosPorPeriodo(Vinculo vinculo, LocalDate inicio, LocalDate fim,
                                               HistoricoService historicoService) {
        List<Ponto> pontos = new ArrayList<>();
        LocalDate dataAtual = inicio;
        while (!dataAtual.isAfter(fim)) {
            Optional<Ponto> pontoOpt = pontoRepository.findById(PontoId.builder().
                    dia(dataAtual).
                    matricula(vinculo.getMatricula()).
                    build());
            if (pontoOpt.isPresent()) {
                pontos.add(pontoOpt.get());
                dataAtual = dataAtual.plusDays(1); // Avança para o próximo dia
                continue;
            }
            var historicos = historicoService.buscarHistoricoDeAcesso(dataAtual,
                    null, vinculo.getCracha(), null, null);
            pontos.add(salvaPontoPorData(vinculo.getMatricula(), dataAtual, historicos));
            dataAtual = dataAtual.plusDays(1); // Avança para o próximo dia
        }
        return pontos;
    }

    @Transactional
    public void salvaPontoDeHoje(Integer matricula, List<HistoricoResponse> historicos) {
        salvaPontoPorData(matricula, LocalDate.now(), historicos);
    }

    @Transactional
    public Ponto salvaPontoPorMatriculaMaisData(Integer matricula, LocalDate data, List<HistoricoResponse> historicos) {
        return salvaPontoPorData(matricula, data, historicos);
    }

    public Ponto adicionaRegistros(Integer matricula, LocalDate dia, List<Registro> registros) {
        Ponto ponto = pontoRepository.buscaPonto(matricula, dia).
                orElseThrow(() ->
                        new PontoInexistenteException("Não existe ponto para matrícula: %d e dia: %s".
                                formatted(matricula, dataParaString(dia))));
        registros.forEach(registro -> registro.setPonto(ponto));
        ponto.getRegistros().addAll(registros);
        return pontoRepository.save(ponto);
    }
}
