package br.jus.trf1.sipe.servidor.domain.service;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.ausencia.ausencia.domain.port.in.AusenciaServicePort;
import br.jus.trf1.sipe.lotacao.domain.service.LotacaoServiceAdapter;
import br.jus.trf1.sipe.lotacao.exceptions.LotacaoNaoTemDiretorException;
import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import br.jus.trf1.sipe.servidor.domain.port.in.ServidorExternoPort;
import br.jus.trf1.sipe.servidor.domain.port.in.ServidorServicePort;
import br.jus.trf1.sipe.servidor.domain.port.out.ServidorPersistencePort;
import br.jus.trf1.sipe.servidor.exceptions.ServidorInexistenteException;
import br.jus.trf1.sipe.usuario.domain.port.in.UsuarioServicePort;
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

    private final UsuarioServicePort usuarioServicePort;
    private final ServidorPersistencePort servidorPersistencePort;
    private final ServidorExternoPort servidorExternoPort;
    private final AusenciaServicePort ausenciaServicePort;
    private final LotacaoServiceAdapter lotacaoServiceAdapter;


    public ServidorServiceAdapter(UsuarioServicePort usuarioServicePort, ServidorPersistencePort servidorPersistencePort,
                                  ServidorExternoPort servidorExternoPort, AusenciaServicePort ausenciaServicePort,
                                  LotacaoServiceAdapter lotacaoServiceAdapter) {
        this.usuarioServicePort = usuarioServicePort;
        this.servidorPersistencePort = servidorPersistencePort;
        this.servidorExternoPort = servidorExternoPort;
        this.ausenciaServicePort = ausenciaServicePort;
        this.lotacaoServiceAdapter = lotacaoServiceAdapter;
    }

    @Override
    public Servidor atualizaDadosDoSarh(String matricula) {
        var usuario = usuarioServicePort.buscaPorMatricula(matricula);
        var servidor = (Servidor) usuario;
        return atualizaDadosDoSarh(servidor);
    }

    @Override
    public Servidor atualizaDadosDoSarh(Servidor servidor) {
        var matricula = servidor.getMatricula();
        log.info("Atualizando usuário com matricula: {}", matricula);
        var servidorExternoOpt = servidorExternoPort.buscaServidorExterno(matricula);
        if (servidorExternoOpt.isPresent()) {
            var servidorExterno = servidorExternoOpt.get();
            lotacaoServiceAdapter.atualizarLotacao(servidor.getLotacao(), servidorExterno.getLotacao());
            servidor = createServidor(servidor, servidorExterno);
            return servidorPersistencePort.salva(servidor);
        }
        log.info("Não foi possível atualizar os dados do servidor : {}", matricula);
        return servidor;
    }

    @Override
    public Servidor servidorAtual() {
        var usuarioAtual = usuarioServicePort.getUsuarioAutenticado();
        var servidorAtualOpt = servidorPersistencePort.buscaPorId(usuarioAtual.getId());
        if (servidorAtualOpt.isPresent()) {
            return servidorAtualOpt.get();
        }
        throw new ServidorInexistenteException(usuarioAtual.getId());
    }

    @Override
    public Servidor buscaPorMatricula(String matricula) {
        Optional<Servidor> optServidor = servidorPersistencePort.buscaPorMatricula(matricula);
        return optServidor.orElseGet(() -> atualizaDadosDoSarh(matricula));
    }


    @Override
    public List<Servidor> listar() {
        if (usuarioServicePort.permissaoDiretor()) {
            log.info("listar por lotação do Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoServiceAdapter.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorPersistencePort.listarPorLotacoes(idsLotacoes);
        }
        log.info("listarAll: Outros");
        return servidorPersistencePort.listarTodos();
    }

    @Override
    public List<Servidor> listar(Integer idLotacaoPai) {
        log.info("listar Por Lotação id: {}", idLotacaoPai);
        if (usuarioServicePort.permissaoDiretor()) {
            log.info("listar por lotação do Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoServiceAdapter.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorPersistencePort.listarPorLotacoes(idsLotacoes);
        }
        var idsLotacoes = lotacaoServiceAdapter.getLotacaos(idLotacaoPai);
        return servidorPersistencePort.listarPorLotacoes(idsLotacoes);
    }

    @Override
    public List<Servidor> listar(String nome,
                                 Integer cracha,
                                 String matricula,
                                 Integer idLotacao) {
        if (usuarioServicePort.permissaoDiretor()) {
            log.info("Paginar filtrado: Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoServiceAdapter.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorPersistencePort.listarPorNomeOuCrachaOuMatriculaEeLotacoes(nome, cracha, matricula, idsLotacoes);
        }
        log.info("Paginar filtrado: Outros");
        var idsLotacoes = lotacaoServiceAdapter.getLotacaos(idLotacao);
        return servidorPersistencePort.listarPorNomeOuCrachaOuMatriculaEeLotacoes(nome, cracha, matricula, idsLotacoes);
    }


    @Override
    public List<Servidor> paginar(int page, int size) {
        if (usuarioServicePort.permissaoDiretor()) {
            log.info("listar por lotação do Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoServiceAdapter.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorPersistencePort.paginarPorLotacoes(idsLotacoes, page, size);
        }
        log.info("listarAll: Outros");
        return servidorPersistencePort.paginar(page, size);
    }

    @Override
    public List<Servidor> paginar(String nome,
                                  Integer cracha,
                                  String matricula,
                                  int page, int size) {
        if (usuarioServicePort.permissaoDiretor()) {
            log.info("Paginar filtrado: Diretor");
            var servidorAtual = servidorAtual();
            return servidorPersistencePort.paginarPorNomeOuCrachaOuMatriculaEeIdLotacao(nome, cracha, matricula, servidorAtual.getLotacao().getId(), page, size);
        }
        log.info("Paginar filtrado: Outros");
        return servidorPersistencePort.paginarPorNomeOuCrachaOuMatricula(nome, cracha, matricula, page, size);
    }

    @Override
    public Servidor atualizaAusenciasServidorNoPeriodo(Servidor servidor, LocalDate dataInicio, LocalDate dataFim) {
        log.info("Vinculando ausencias do servidor {}", servidor);
        List<Ausencia> ausenciasAtulizadas = ausenciaServicePort.atualizaNoPeriodo(servidor.getMatricula(), dataInicio, dataFim);
        servidor.setAusencias(new ArrayList<>(ausenciasAtulizadas));
        return servidor;
    }

    public Servidor buscaDiretorLotacao(Integer idLotacao) {
        Optional<Servidor> optServidor = servidorPersistencePort.buscaDiretorLotacao(idLotacao);

        if (optServidor.isPresent()) {
            return optServidor.get();
        }
        throw new LotacaoNaoTemDiretorException(idLotacao);
    }
}
