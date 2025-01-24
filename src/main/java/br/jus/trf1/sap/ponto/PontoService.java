package br.jus.trf1.sap.ponto;

import br.jus.trf1.sap.historico.HistoricoService;
import br.jus.trf1.sap.ponto.registro.Registro;
import br.jus.trf1.sap.ponto.registro.exceptions.RegistroExistenteSalvoEmPontoDifenteException;
import br.jus.trf1.sap.ponto.registro.exceptions.RegistroInexistenteException;
import br.jus.trf1.sap.ponto.registro.RegistroRepository;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static br.jus.trf1.sap.util.DateTimeUtils.formatarParaString;

@Slf4j
@Service
public class PontoService {

    private PontoRepository pontoRepository;
    private RegistroRepository registroRepository;
    private HistoricoService historicoService;
    private VinculoRepository vinculoRepository;

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

    public List<Ponto> buscarPontos(Integer matricula, LocalDate inicio, LocalDate fim) {
        log.info("Lista de Pontos - {} - {} - {} ", formatarParaString(inicio), formatarParaString(fim), matricula);
        return pontoRepository.buscarPontosPorMatriculaMaisRangeDeData(matricula, inicio, fim);
    }

    public Ponto salvarPonto(Ponto ponto) {
        log.info("Salvando Ponto - {} - {} ", formatarParaString(ponto.getId().getDia()), ponto.getId().getMatricula());
        return pontoRepository.save(ponto);
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

}
