package br.jus.trf1.sipe.registro;

import br.jus.trf1.sipe.externo.coletor.historico.HistoricoService;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.registro.exceptions.RegistroExistenteException;
import br.jus.trf1.sipe.registro.exceptions.RegistroInexistenteException;
import br.jus.trf1.sipe.usuario.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_SAIDA_TEMPO;
import static br.jus.trf1.sipe.comum.util.DataTempoUtil.paraString;

@Slf4j
@Service
public class RegistroService {

    private final PontoService pontoService;
    private final UsuarioService usuarioService;
    private final HistoricoService historicoService;
    private final RegistroRepository registroRepository;


    public RegistroService(UsuarioService usuarioService,
                           HistoricoService historicoService,
                           RegistroRepository registroRepository,
                           PontoService pontoService) {
        this.usuarioService = usuarioService;
        this.historicoService = historicoService;
        this.registroRepository = registroRepository;
        this.pontoService = pontoService;
    }

    public Registro buscaRegistroPorId(Long id) {
        return registroRepository.findById(id).orElseThrow(() -> new RegistroInexistenteException(id));
    }

    public Boolean existe(Long id) {
        return registroRepository.existsById(id);
    }

    public Registro criarNovoRegistro(Registro registro) {
        log.debug("Criando Registro - {}", paraString(registro.getHora(), PADRAO_SAIDA_TEMPO));
        if (registro.getCodigoAcesso() == null) {
            registro.setVersao(1);
            Registro registroSalvo = registroRepository.save(registro);
            log.debug("Registro id = {}  criado com sucesso", registroSalvo.getId());
            return registroSalvo;
        }
        var registroOptional = registroRepository.findByCodigoAcesso(registro.getCodigoAcesso());
        if (registroOptional.isPresent()) {
            var registroAntigo = registroOptional.get();
            if (registroAntigo.getPonto().equals(registro.getPonto())) {
                throw new RegistroExistenteException(registro);
            }
            registro.setId(null);
            registro.setCodigoAcesso(null);
            registro.setVersao(registroAntigo.getVersao() + 1);
            var registroAtualizado = registroRepository.save(registro);
            registroAntigo.setRegistroAnterior(registroAtualizado);
            registroRepository.save(registroAntigo);
            return registroAtualizado;
        }
        registro.setVersao(1);
        return registroRepository.save(registro);
    }

    public Registro atualizaRegistro(Registro registro) {
        log.debug("Atualizando Registro - {} - {} - {}", registro.getId(),
                paraString(registro.getHora(), PADRAO_SAIDA_TEMPO),
                registro.getCodigoAcesso());

        if (registro.getId() != null) {
            var registroAnterior = registroRepository.findById(registro.getId()).orElseThrow(() -> new
                    RegistroInexistenteException(registro));
            registro.setId(null);
            registro.setCodigoAcesso(null);
            registro.setVersao(registroAnterior.getVersao() + 1);
            var registroAtualizado = registroRepository.save(registro);
            registroAnterior.setRegistroAnterior(registroAtualizado);
            return registroRepository.save(registroAnterior);

        }
        throw new IllegalArgumentException("Registro com id nulo");
    }

    public List<Registro> listarRegistrosPonto(String matricula, LocalDate dia) {
        return registroRepository.listarRegistrosPonto(matricula, dia);
    }

    public List<Registro> atualizaRegistrosNovos(String matricula, LocalDate dia) {
        var ponto = pontoService.buscaPonto(matricula, dia);

        var registrosAtuais = registroRepository.listarRegistrosPonto(matricula, dia);
        var vinculo = usuarioService.buscaPorMatricula(matricula);
        var historicos = historicoService.buscarHistoricoDeAcesso(
                dia, null, vinculo.getCracha(), null, null);
        var registros = historicos.stream().
                filter(historico ->
                        {
                            log.debug("historico {}", historico);
                            return registrosAtuais.stream().noneMatch(r ->
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
                                .ponto(ponto)
                                .build()
                )
                .toList();

        registroRepository.saveAll(registros);

        return registroRepository.listarRegistrosPonto(matricula, dia);

    }

    public List<Registro> adicionaNovosRegistros(String matricula, LocalDate dia, List<Registro> registros) {

        final var ponto = pontoService.buscaPonto(matricula, dia);
        ponto.getRegistros().addAll(registros.stream().
                map(registro -> addPonto(registro, ponto)).toList());
        var pontoSalvo = pontoService.salvaPonto(ponto);

        return pontoSalvo.getRegistros();
    }

    public Registro addPonto(Registro registro, Ponto ponto) {
        return Registro.builder()
                .id(registro.getId())
                .hora(registro.getHora())
                .sentido(registro.getSentido().getCodigo())
                .codigoAcesso(registro.getCodigoAcesso())
                .versao(registro.getVersao())
                .registroAnterior(registro.getRegistroAnterior())
                .ponto(ponto)
                .build();
    }

    public Registro atualizaRegistro(String matricula, LocalDate dia, Registro registroAtualizado) {
        var ponto = pontoService.buscaPonto(matricula, dia);
        registroAtualizado.setPonto(ponto);
        return registroRepository.save(registroAtualizado);
    }
}
