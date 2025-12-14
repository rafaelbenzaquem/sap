package br.jus.trf1.sipe.servidor.domain.service;

import br.jus.trf1.sipe.ausencia.AusenciaRepository;
import br.jus.trf1.sipe.ausencia.externo.jsrh.AusenciaExternaService;
import br.jus.trf1.sipe.lotacao.domain.service.LotacaoServiceAdapter;
import br.jus.trf1.sipe.lotacao.exceptions.LotacaoNaoTemDiretorException;
import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import br.jus.trf1.sipe.servidor.domain.port.in.ServidorExternoPort;
import br.jus.trf1.sipe.servidor.domain.port.in.ServidorServicePort;
import br.jus.trf1.sipe.servidor.domain.port.out.ServidorRepositoryPort;
import br.jus.trf1.sipe.servidor.exceptions.ServidorInexistenteException;
import br.jus.trf1.sipe.servidor.infrastructure.persistence.ServidorJpaMapper;
import br.jus.trf1.sipe.usuario.domain.service.UsuarioServiceAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.jus.trf1.sipe.servidor.domain.service.ServidorCreator.createServidor;

@Slf4j
@Service
public class ServidorServiceAdapter implements ServidorServicePort {

    private final UsuarioServiceAdapter usuarioService;
    private final ServidorRepositoryPort servidorRepositoryPort;
    private final ServidorExternoPort servidorExternoPort;
    private final AusenciaExternaService ausenciaExternaService;
    private final AusenciaRepository ausenciaRepository;
    private final LotacaoServiceAdapter lotacaoServiceAdapter;

    public ServidorServiceAdapter(UsuarioServiceAdapter usuarioService, ServidorRepositoryPort servidorRepositoryPort,
                                  ServidorExternoPort servidorExternoPort, AusenciaExternaService ausenciaExternaService,
                                  AusenciaRepository ausenciaRepository, LotacaoServiceAdapter lotacaoServiceAdapter) {
        this.usuarioService = usuarioService;
        this.servidorRepositoryPort = servidorRepositoryPort;
        this.servidorExternoPort = servidorExternoPort;
        this.ausenciaExternaService = ausenciaExternaService;
        this.ausenciaRepository = ausenciaRepository;
        this.lotacaoServiceAdapter = lotacaoServiceAdapter;
    }

    @Override
    public Servidor atualizaDadosDoSarh(String matricula) {
        log.info("Buscando usuário com matricula: {}", matricula);
        var usuario = usuarioService.buscaPorMatricula(matricula);
        var servidor = (Servidor) usuario;
        var servidorExternoOpt = servidorExternoPort.buscaServidorExterno(matricula);
        if (servidorExternoOpt.isPresent()) {
            var servidorExterno = servidorExternoOpt.get();
            lotacaoServiceAdapter.atualizarLotacao(servidor.getLotacao(), servidorExterno.getLotacao());
            servidor = createServidor(servidor, servidorExterno);
            return servidorRepositoryPort.salva(servidor);
        }
        log.info("Não foi possível atualizar os dados do servidor : {}", matricula);
        return (Servidor) usuario;
    }


    @Override
    public Servidor servidorAtual() {
        var usuarioAtual = usuarioService.getUsuarioAutenticado();
        var servidorAtualOpt = servidorRepositoryPort.buscaPorId(usuarioAtual.getId());
        if (servidorAtualOpt.isPresent()) {
            return servidorAtualOpt.get();
        }
        throw new ServidorInexistenteException(usuarioAtual.getId());
    }

    @Override
    public Servidor buscaPorMatricula(String matricula) {
        Optional<Servidor> optServidor = servidorRepositoryPort.buscaPorMatricula(matricula);
        return optServidor.orElseGet(() -> atualizaDadosDoSarh(matricula));
    }


    @Override
    public List<Servidor> listar() {
        if (usuarioService.permissaoDiretor()) {
            log.info("listar por lotação do Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoServiceAdapter.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorRepositoryPort.listarPorLotacoes(idsLotacoes);
        }
        log.info("listarAll: Outros");
        return servidorRepositoryPort.listarTodos();
    }

    @Override
    public List<Servidor> listar(Integer idLotacaoPai) {
        log.info("listar Por Lotação id: {}", idLotacaoPai);
        if (usuarioService.permissaoDiretor()) {
            log.info("listar por lotação do Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoServiceAdapter.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorRepositoryPort.listarPorLotacoes(idsLotacoes);
        }
        var idsLotacoes = lotacaoServiceAdapter.getLotacaos(idLotacaoPai);
        return servidorRepositoryPort.listarPorLotacoes(idsLotacoes);
    }

    @Override
    public List<Servidor> listar(String nome,
                                 Integer cracha,
                                 String matricula,
                                 Integer idLotacao) {
        if (usuarioService.permissaoDiretor()) {
            log.info("Paginar filtrado: Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoServiceAdapter.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorRepositoryPort.listarPorNomeOuCrachaOuMatriculaEeLotacoes(nome, cracha, matricula, idsLotacoes);
        }
        log.info("Paginar filtrado: Outros");
        var idsLotacoes = lotacaoServiceAdapter.getLotacaos(idLotacao);
        return servidorRepositoryPort.listarPorNomeOuCrachaOuMatriculaEeLotacoes(nome, cracha, matricula, idsLotacoes);
    }


    @Override
    public List<Servidor> paginar(int page, int size) {
        if (usuarioService.permissaoDiretor()) {
            log.info("listar por lotação do Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoServiceAdapter.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorRepositoryPort.paginarPorLotacoes(idsLotacoes, page, size);
        }
        log.info("listarAll: Outros");
        return servidorRepositoryPort.paginar(page, size);
    }

    @Override
    public List<Servidor> paginar(String nome,
                                  Integer cracha,
                                  String matricula,
                                  int page, int size) {
        if (usuarioService.permissaoDiretor()) {
            log.info("Paginar filtrado: Diretor");
            var servidorAtual = servidorAtual();
            return servidorRepositoryPort.paginarPorNomeOuCrachaOuMatriculaEeIdLotacao(nome, cracha, matricula, servidorAtual.getLotacao().getId(), page, size);
        }
        log.info("Paginar filtrado: Outros");
        return servidorRepositoryPort.paginarPorNomeOuCrachaOuMatricula(nome, cracha, matricula, page, size);
    }

    @Override
    public Servidor vinculaAusenciasServidorNoPeriodo(Servidor servidor, LocalDate dataInicio, LocalDate dataFim) {
        log.info("Vinculando ausencias do servidor {}", servidor);

        var ausenciasExternas = ausenciaExternaService.
                buscaAusenciasServidorPorPeriodo(servidor.getMatricula(), dataInicio, dataFim);
        var novasAusencias = ausenciasExternas.stream().map(auEx -> auEx.toModel(ServidorJpaMapper.toEntity(servidor))).toList();

        var ausenciasExistentes = ausenciaRepository.listaAusenciasPorServidorMaisPeriodo(ServidorJpaMapper.toEntity(servidor), dataInicio, dataFim);

        var ausenciasParaDelete = ausenciasExistentes.stream().filter(ae -> !novasAusencias.contains(ae)).toList();
        var ausenciasParaSalve = novasAusencias.stream().filter(ae -> !ausenciasExistentes.contains(ae)).toList();

        if (!ausenciasParaDelete.isEmpty()) {
            ausenciaRepository.deleteAll(ausenciasParaDelete);
        }

        if (!ausenciasParaSalve.isEmpty()) {
            ausenciasParaSalve.forEach(ausenciaRepository::save);
        }

        var todasAusenciasDoPeriodo = ausenciaRepository.listaAusenciasPorServidorMaisPeriodo(ServidorJpaMapper.toEntity(servidor), dataInicio, dataFim);
        servidor.setAusencias(new ArrayList<>(todasAusenciasDoPeriodo));
        return servidor;
    }

    public Servidor buscaDiretorLotacao(Integer idLotacao) {
        Optional<Servidor> optServidor = servidorRepositoryPort.buscaDiretorLotacao(idLotacao);

        if (optServidor.isPresent()) {
            return optServidor.get();
        }
        throw new LotacaoNaoTemDiretorException(idLotacao);
    }
}
