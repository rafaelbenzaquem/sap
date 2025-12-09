package br.jus.trf1.sipe.servidor;

import br.jus.trf1.sipe.ausencia.AusenciaRepository;
import br.jus.trf1.sipe.ausencia.externo.jsrh.AusenciaExternaService;
import br.jus.trf1.sipe.servidor.domain.port.out.ServidorRepositoryPort;
import br.jus.trf1.sipe.servidor.externo.jsarh.ServidorExternoService;
import br.jus.trf1.sipe.lotacao.LotacaoNaoTemDiretorDireto;
import br.jus.trf1.sipe.lotacao.LotacaoService;
import br.jus.trf1.sipe.servidor.exceptions.ServidorInexistenteException;
import br.jus.trf1.sipe.servidor.infrastructure.persistence.ServidorJpa;
import br.jus.trf1.sipe.usuario.domain.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.jus.trf1.sipe.servidor.ServidorCreator.createServidor;

@Slf4j
@Service
public class ServidorService {

    private final UsuarioService usuarioService;
    private final ServidorRepositoryPort servidorRepositoryPort;
    private final ServidorExternoService servidorExternoService;
    private final AusenciaExternaService ausenciaExternaService;
    private final AusenciaRepository ausenciaRepository;
    private final LotacaoService lotacaoService;

    public ServidorService(UsuarioService usuarioService, ServidorRepositoryPort servidorRepositoryPort,
                           ServidorExternoService servidorExternoService, AusenciaExternaService ausenciaExternaService,
                           AusenciaRepository ausenciaRepository, LotacaoService lotacaoService) {
        this.usuarioService = usuarioService;
        this.servidorRepositoryPort = servidorRepositoryPort;
        this.servidorExternoService = servidorExternoService;
        this.ausenciaExternaService = ausenciaExternaService;
        this.ausenciaRepository = ausenciaRepository;
        this.lotacaoService = lotacaoService;
    }

    @Transactional
    public ServidorJpa atualizaDadosNoSarh(String matricula) {
        log.info("Buscando usuário com matricula: {}", matricula);
        var servidor = (ServidorJpa) usuarioService.buscaPorMatricula(matricula);
        var servidorExternoOpt = servidorExternoService.buscaServidorExterno(matricula);
        if (servidorExternoOpt.isPresent()) {
            var servidorExterno = servidorExternoOpt.get();
            var lotacaoExterna = servidorExterno.getLotacao();
            lotacaoService.atualizarLotacao(servidor.getLotacao(), lotacaoExterna);
            servidor = createServidor(servidor, servidorExterno);
            return servidorRepositoryPort.save(servidor);
        }
        log.info("Não foi possível atualizar os dados do servidor : {}", matricula);
        return servidor;
    }


    public ServidorJpa servidorAtual() {
        var usuarioAtual = usuarioService.getUsuarioAutenticado();
        var servidorAtualOpt = servidorRepositoryPort.findById(usuarioAtual.getId());
        if (servidorAtualOpt.isPresent()) {
            return servidorAtualOpt.get();
        }
        throw new ServidorInexistenteException(usuarioAtual.getId());
    }

    public ServidorJpa buscaPorMatricula(String matricula) {
        Optional<ServidorJpa> optServidor = servidorRepositoryPort.findByMatricula(matricula);
        return optServidor.orElseGet(() -> atualizaDadosNoSarh(matricula));
    }


    public List<ServidorJpa> listar() {
        if (usuarioService.permissaoDiretor()) {
            log.info("listar por lotação do Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoService.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorRepositoryPort.listarPorLotacoes(idsLotacoes);
        }
        log.info("listarAll: Outros");
        return servidorRepositoryPort.listarTodos();
    }

    public List<ServidorJpa> listar(Integer idLotacaoPai) {
        log.info("listar Por Lotação id: {}", idLotacaoPai);
        if (usuarioService.permissaoDiretor()) {
            log.info("listar por lotação do Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoService.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorRepositoryPort.listarPorLotacoes(idsLotacoes);
        }
        var idsLotacoes = lotacaoService.getLotacaos(idLotacaoPai);
        return servidorRepositoryPort.listarPorLotacoes(idsLotacoes);
    }

    public List<ServidorJpa> listar(String nome,
                                    Integer cracha,
                                    String matricula,
                                    Integer idLotacao) {
        if (usuarioService.permissaoDiretor()) {
            log.info("Paginar filtrado: Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoService.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorRepositoryPort.listarPorNomeOuCrachaOuMatriculaEeLotacoes(nome, cracha, matricula, idsLotacoes);
        }
        log.info("Paginar filtrado: Outros");
        var idsLotacoes = lotacaoService.getLotacaos(idLotacao);
        return servidorRepositoryPort.listarPorNomeOuCrachaOuMatriculaEeLotacoes(nome, cracha, matricula, idsLotacoes);
    }


    public Page<ServidorJpa> paginar(Pageable pageable) {
        if (usuarioService.permissaoDiretor()) {
            log.info("listar por lotação do Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoService.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorRepositoryPort.paginarPorLotacoes(idsLotacoes, pageable);
        }
        log.info("listarAll: Outros");
        return servidorRepositoryPort.findAll(pageable);
    }

    public Page<ServidorJpa> paginar(String nome,
                                     Integer cracha,
                                     String matricula,
                                     Pageable pageable) {
        if (usuarioService.permissaoDiretor()) {
            log.info("Paginar filtrado: Diretor");
            var servidorAtual = servidorAtual();
            return servidorRepositoryPort.paginarPorNomeOuCrachaOuMatriculaEeIdLotacao(nome, cracha, matricula, servidorAtual.getLotacao().getId(), pageable);
        }
        log.info("Paginar filtrado: Outros");
        return servidorRepositoryPort.paginarPorNomeOuCrachaOuMatricula(nome, cracha, matricula, pageable);
    }

    public ServidorJpa vinculaAusenciasServidorNoPeriodo(ServidorJpa servidor, LocalDate dataInicio, LocalDate dataFim) {
        log.info("Vinculando ausencias do servidor {}", servidor);

        var ausenciasExternas = ausenciaExternaService.
                buscaAusenciasServidorPorPeriodo(servidor.getMatricula(), dataInicio, dataFim);
        var novasAusencias = ausenciasExternas.stream().map(auEx -> auEx.toModel(servidor)).toList();

        var ausenciasExistentes = ausenciaRepository.listaAusenciasPorServidorMaisPeriodo(servidor, dataInicio, dataFim);

        var ausenciasParaDelete = ausenciasExistentes.stream().filter(ae -> !novasAusencias.contains(ae)).toList();
        var ausenciasParaSalve = novasAusencias.stream().filter(ae -> !ausenciasExistentes.contains(ae)).toList();

        if (!ausenciasParaDelete.isEmpty()) {
            ausenciaRepository.deleteAll(ausenciasParaDelete);
        }

        if (!ausenciasParaSalve.isEmpty()) {
            ausenciasParaSalve.forEach(ausenciaRepository::save);
        }

        var todasAusenciasDoPeriodo = ausenciaRepository.listaAusenciasPorServidorMaisPeriodo(servidor, dataInicio, dataFim);
        servidor.setAusencias(new ArrayList<>(todasAusenciasDoPeriodo));
        return servidor;
    }

    public ServidorJpa buscaDiretorLotacao(Integer idLotacao) {
        Optional<ServidorJpa> optServidor = servidorRepositoryPort.buscaDiretorLotacao(idLotacao);

        if (optServidor.isPresent()) {
            return optServidor.get();
        }
        throw new LotacaoNaoTemDiretorDireto(idLotacao);
    }
}
