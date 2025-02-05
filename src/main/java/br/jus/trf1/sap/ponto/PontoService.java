package br.jus.trf1.sap.ponto;

import br.jus.trf1.sap.historico.HistoricoService;
import br.jus.trf1.sap.registro.Registro;
import br.jus.trf1.sap.registro.exceptions.RegistroExistenteSalvoEmPontoDifenteException;
import br.jus.trf1.sap.registro.exceptions.RegistroInexistenteException;
import br.jus.trf1.sap.registro.RegistroRepository;
import br.jus.trf1.sap.vinculo.Vinculo;
import br.jus.trf1.sap.vinculo.VinculoInexistenteException;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import static br.jus.trf1.sap.util.DateTimeUtils.formatarParaString;

@Slf4j
@Service
public class PontoService {

    private final PontoRepository pontoRepository;
    private final RegistroRepository registroRepository;
    private final HistoricoService historicoService;
    private final VinculoRepository vinculoRepository;

    public PontoService(PontoRepository pontoRepository, RegistroRepository registroRepository,
                        HistoricoService historicoService, VinculoRepository vinculoRepository) {
        this.pontoRepository = pontoRepository;
        this.registroRepository = registroRepository;
        this.historicoService = historicoService;
        this.vinculoRepository = vinculoRepository;
    }

    public Optional<Ponto> buscarPonto(Integer matricula, LocalDate dia) {
        log.info("Buscando Ponto - {} - {} ", formatarParaString(dia), matricula);
        return pontoRepository.findById(
                PontoId.builder().
                        matricula(matricula).
                        dia(dia).
                        build()
        );
    }

    public Optional<Ponto> buscarAtualizarRegistrosPonto(Integer matricula, LocalDate dia) {
        log.info("Buscar Atualizacao Ponto - {} - {} ", formatarParaString(dia), matricula);
        var optPonto = pontoRepository.buscarPontoPorMatriculaMaisDia(matricula, dia);
        log.info("ponto {}", optPonto.isPresent() ? "foi encontrato!" : "não foi encontrato!");

        if (optPonto.isPresent()) {
            Ponto ponto = optPonto.get();
            log.info("ponto {}", ponto);
            var vinculo = vinculoRepository.findVinculoByMatricula(matricula).orElseThrow(RegistroInexistenteException::new);
            log.info("vinculo {}", vinculo);
            var historicos = historicoService.buscarHistoricoDeAcesso(
                    formatarParaString(dia), vinculo.getCracha(), null, null);
            var registros = historicos.stream().
                    filter(hr ->
                            {
                                log.info("historico {}", hr);
                                return ponto.getRegistros().stream().noneMatch(r ->
                                        {
                                            var filtered = Objects.equals(hr.acesso(), r.getCodigoAcesso());
                                            log.info("registro {} - {}",
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

            registros.forEach(registro -> {
                log.info("Salvando o registro {}", registro);
                ponto.getRegistros().add(registro);
                registroRepository.save(registro);
            });

            return Optional.of(ponto);
        }

        return Optional.empty();
    }


    public List<Ponto> buscarPontos(Integer matricula, LocalDate inicio, LocalDate fim) {
        log.info("Lista de Pontos - {} - {} - {} ", formatarParaString(inicio), formatarParaString(fim), matricula);
        return pontoRepository.buscarPontosPorMatriculaMaisRangeDeData(matricula, inicio, fim);
    }

    public Ponto salvarPonto(Ponto ponto) {
        log.info("Salvando Ponto - {} - {} ", formatarParaString(ponto.getId().getDia()), ponto.getId().getMatricula());
        return pontoRepository.save(ponto);
    }


    @Transactional
    public Ponto salvarPontoDeUmVinculoPorData(Vinculo vinculo, LocalDate data) {
        var historicos = historicoService.buscarHistoricoDeAcesso(
                formatarParaString(data)
                , vinculo.getCracha(), null, null);
        var ponto = pontoRepository.save(
                Ponto.builder()
                        .id(PontoId.builder()
                                .dia(data)
                                .matricula(vinculo.getMatricula())
                                .build())
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
        var registrosSalvos = registros.stream().map(this::criarNovoRegistro).toList();
        ponto.setRegistros(registrosSalvos);
        return ponto;
    }

    public Registro criarNovoRegistro(Registro registro) {
        log.info("Criando novo registro");
        if (registro.getCodigoAcesso() == null) {
            registro.setVersao(1);
            Registro registroSalvo = registroRepository.save(registro);
            log.info("Registro id = {}  criado com sucesso", registroSalvo.getId());
            return registroSalvo;
        }
        var registroOptional = registroRepository.findByCodigoAcesso(registro.getCodigoAcesso());
        if (registroOptional.isPresent()) {
            var registroAntigo = registroOptional.get();
            if (registroAntigo.getPonto().equals(registro.getPonto())) {
                throw new RegistroExistenteSalvoEmPontoDifenteException(
                        "Registro foi salvo anteriormente no Ponto %s - %s".
                                formatted(registroAntigo.getPonto().getId().getDia(),
                                        registroAntigo.getPonto().getId().getMatricula()));
            }
            registro.setId(null);
            registro.setCodigoAcesso(null);
            registro.setVersao(registroAntigo.getVersao() + 1);
            var registroAtualizado = registroRepository.save(registro);
            registroAntigo.setRegistroAtualizado(registroAtualizado);
            registroRepository.save(registroAntigo);
            return registroAtualizado;
        }
        registro.setVersao(1);
        return registroRepository.save(registro);
    }

    public Registro atualizaRegistro(Registro registro) {
        log.info("Atualizando Registro - {} - {} - {}", registro.getId()
                , formatarParaString(registro.getHora())
                , registro.getCodigoAcesso());

        if (registro.getId() != null) {
            var registroOptPorId = registroRepository.findById(registro.getId());

            var registroAnterior = registroOptPorId.orElseThrow(() -> new
                    RegistroInexistenteException("Registro id - %s não existe!".formatted(registro.getId())));
            registro.setId(null);
            registro.setCodigoAcesso(null);
            registro.setVersao(registroAnterior.getVersao() + 1);
            var registroAtualizado = registroRepository.save(registro);
            registroAnterior.setRegistroAtualizado(registroAtualizado);
            return registroRepository.save(registroAnterior);

        }
        throw new IllegalArgumentException("Registro com id nulo");
    }

    @Transactional
    public void salvarPontoDeHojeDeUmVinculo(Vinculo vinculo) {
        salvarPontoDeUmVinculoPorData(vinculo, LocalDate.now());
    }

    @Transactional
    public Ponto salvarPontoPorMatriculaMaisData(Integer matricula, LocalDate data) {
        var optVinculo = vinculoRepository.findVinculoByMatricula(matricula);
        if (optVinculo.isPresent()) {
            return  salvarPontoDeUmVinculoPorData(optVinculo.get(),data);
        }
        throw new VinculoInexistenteException("Não foi possível encontrar vinculo de matrícula:"+matricula);
    }
}
