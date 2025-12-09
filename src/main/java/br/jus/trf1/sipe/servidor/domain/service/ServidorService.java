package br.jus.trf1.sipe.servidor.domain.service;

import br.jus.trf1.sipe.lotacao.domain.port.out.LotacaoPort;
import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import br.jus.trf1.sipe.servidor.domain.port.in.ServidorServicePort;
import br.jus.trf1.sipe.servidor.domain.port.out.*;
import br.jus.trf1.sipe.usuario.domain.port.in.UsuarioServicePort;
import br.jus.trf1.sipe.lotacao.LotacaoNaoTemDiretorDireto;
import br.jus.trf1.sipe.servidor.exceptions.ServidorInexistenteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static br.jus.trf1.sipe.servidor.domain.service.ServidorMapping.toModel;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServidorService implements ServidorServicePort {

    private final UsuarioServicePort usuarioService;
    private final ServidorRepositoryPort servidorRepository;
    private final ServidorExternoPort servidorExternoPort;
    private final AusenciaExternaPort ausenciaExternaPort;
    private final AusenciaRepositoryPort ausenciaRepository;
    private final LotacaoPort lotacaoPort;

    @Override
    @Transactional
    public Servidor atualizaDadosNoSarh(String matricula) {
        log.info("Buscando usuário com matricula: {}", matricula);
        var servidor = (Servidor) usuarioService.buscaPorMatricula(matricula);
        var servidorExternoOpt = servidorExternoPort.buscaServidorExterno(matricula);
        if (servidorExternoOpt.isPresent()) {
            var servidorExterno = servidorExternoOpt.get();
            var lotacaoExterna = servidorExterno.getLotacao();
            lotacaoPort.atualizarLotacao(servidor.getLotacao(), lotacaoExterna);
            servidor = toModel(servidor, servidorExterno);
            return servidorRepository.save(servidor);
        }
        log.info("Não foi possível atualizar os dados do servidor : {}", matricula);
        return servidor;
    }

    @Override
    public Servidor servidorAtual() {
        var usuarioAtual = usuarioService.getUsuarioAtual();
        return servidorRepository.findById(usuarioAtual.getId())
                .orElseThrow(() -> new ServidorInexistenteException(usuarioAtual.getId()));
    }

    @Override
    public Servidor buscaPorMatricula(String matricula) {
        return servidorRepository.findByMatricula(matricula)
                .orElseGet(() -> atualizaDadosNoSarh(matricula));
    }

    @Override
    public List<Servidor> listar() {
        if (usuarioService.permissaoDiretor()) {
            log.info("listar por lotação do Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoPort.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorRepository.listarPorLotacoes(idsLotacoes);
        }
        log.info("listarAll: Outros");
        return servidorRepository.listarTodos();
    }

    @Override
    public List<Servidor> listar(Integer idLotacaoPai) {
        log.info("listar Por Lotação id: {}", idLotacaoPai);
        if (usuarioService.permissaoDiretor()) {
            log.info("listar por lotação do Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoPort.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorRepository.listarPorLotacoes(idsLotacoes);
        }
        var idsLotacoes = lotacaoPort.getLotacaos(idLotacaoPai);
        return servidorRepository.listarPorLotacoes(idsLotacoes);
    }

    @Override
    public List<Servidor> listar(String nome, Integer cracha, String matricula, Integer idLotacao) {
        if (usuarioService.permissaoDiretor()) {
            log.info("Paginar filtrado: Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoPort.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorRepository.listarPorNomeOuCrachaOuMatriculaEeLotacoes(nome, cracha, matricula, idsLotacoes);
        }
        log.info("Paginar filtrado: Outros");
        var idsLotacoes = lotacaoPort.getLotacaos(idLotacao);
        return servidorRepository.listarPorNomeOuCrachaOuMatriculaEeLotacoes(nome, cracha, matricula, idsLotacoes);
    }

    @Override
    public Page<Servidor> paginar(Pageable pageable) {
        if (usuarioService.permissaoDiretor()) {
            log.info("listar por lotação do Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoPort.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorRepository.paginarPorLotacoes(idsLotacoes, pageable);
        }
        log.info("listarAll: Outros");
        return servidorRepository.findAll(pageable); // Assumindo que o JpaRepository base está disponível no adaptador
    }

    @Override
    public Page<Servidor> paginar(String nome, Integer cracha, String matricula, Pageable pageable) {
        if (usuarioService.permissaoDiretor()) {
            log.info("Paginar filtrado: Diretor");
            var servidorAtual = servidorAtual();
            return servidorRepository.paginarPorNomeOuCrachaOuMatriculaEeIdLotacao(nome, cracha, matricula, servidorAtual.getLotacao().getId(), pageable);
        }
        log.info("Paginar filtrado: Outros");
        return servidorRepository.paginarPorNomeOuCrachaOuMatricula(nome, cracha, matricula, pageable);
    }

    @Override
    public Servidor vinculaAusenciasServidorNoPeriodo(Servidor servidor, LocalDate dataInicio, LocalDate dataFim) {
        log.info("Vinculando ausencias do servidor {}", servidor);
        var ausenciasExternas = ausenciaExternaPort.buscaAusenciasServidorPorPeriodo(servidor.getMatricula(), dataInicio, dataFim);
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

    @Override
    public Servidor buscaDiretorLotacao(Integer idLotacao) {
        return servidorRepository.buscaDiretorLotacao(idLotacao)
                .orElseThrow(() -> new LotacaoNaoTemDiretorDireto(idLotacao));
    }
    
    @Override
    public Page<Servidor> paginar(int page, int size) {
        return paginar(PageRequest.of(page, size));
    }
}