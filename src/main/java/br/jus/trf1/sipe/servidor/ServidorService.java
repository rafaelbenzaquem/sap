package br.jus.trf1.sipe.servidor;

import br.jus.trf1.sipe.ausencia.AusenciaRepository;
import br.jus.trf1.sipe.ausencia.externo.jsrh.AusenciaExternaService;
import br.jus.trf1.sipe.servidor.externo.jsarh.ServidorExternoService;
import br.jus.trf1.sipe.lotacao.LotacaoNaoTemDiretorDireto;
import br.jus.trf1.sipe.lotacao.LotacaoService;
import br.jus.trf1.sipe.servidor.exceptions.ServidorInexistenteException;
import br.jus.trf1.sipe.servidor.externo.jsarh.exceptions.ServidorExternoInexistenteException;
import br.jus.trf1.sipe.usuario.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.jus.trf1.sipe.servidor.ServidorMapping.toModel;

@Slf4j
@Service
public class ServidorService {

    private final UsuarioService usuarioService;
    private final ServidorRepository servidorRepository;
    private final ServidorExternoService servidorExternoService;
    private final AusenciaExternaService ausenciaExternaService;
    private final AusenciaRepository ausenciaRepository;
    private final LotacaoService lotacaoService;

    public ServidorService(UsuarioService usuarioService, ServidorRepository servidorRepository,
                           ServidorExternoService servidorExternoService, AusenciaExternaService ausenciaExternaService,
                           AusenciaRepository ausenciaRepository, LotacaoService lotacaoService) {
        this.usuarioService = usuarioService;
        this.servidorRepository = servidorRepository;
        this.servidorExternoService = servidorExternoService;
        this.ausenciaExternaService = ausenciaExternaService;
        this.ausenciaRepository = ausenciaRepository;
        this.lotacaoService = lotacaoService;
    }


    public Servidor cadastrarNovoServidor(Servidor servidor) {
        var servidorExternoOpt = servidorExternoService.buscaServidorExterno(servidor.getMatricula());
        if (servidorExternoOpt.isPresent()) {
            var servidorExterno = servidorExternoOpt.get();
            var lotacaoExterna = servidorExterno.getLotacao();
            lotacaoService.atualizarLotacao(servidor.getLotacao(), lotacaoExterna);
            servidor = toModel(servidor, servidorExterno);
            return servidorRepository.save(servidor);
        }
        log.error("Não foi possível atualizar os dados do servidor : {}", servidor.getMatricula());
        throw new ServidorExternoInexistenteException("Não existe servidor cadastrado no sara");
    }

    @Transactional
    public Servidor atualizaDadosNoSarh(String matricula) {
        log.info("Buscando usuário com matricula: {}", matricula);
        var servidor = (Servidor) usuarioService.buscaPorMatricula(matricula);
        var servidorExternoOpt = servidorExternoService.buscaServidorExterno(matricula);
        if (servidorExternoOpt.isPresent()) {
            var servidorExterno = servidorExternoOpt.get();
            var lotacaoExterna = servidorExterno.getLotacao();
            lotacaoService.atualizarLotacao(servidor.getLotacao(), lotacaoExterna);
            servidor = toModel(servidor, servidorExterno);
            return servidorRepository.save(servidor);
        }
        log.info("Não foi possível atualizar os dados do servidor : {}", matricula);
        return servidor;
    }


    public Servidor servidorAtual() {
        var usuarioAtual = usuarioService.getUsuarioAtual();
        var servidorAtualOpt = servidorRepository.findById(usuarioAtual.getId());
        if (servidorAtualOpt.isPresent()) {
            return servidorAtualOpt.get();
        }
        throw new ServidorInexistenteException(usuarioAtual.getId());
    }

    public Servidor buscaPorMatricula(String matricula) {
        Optional<Servidor> optServidor = servidorRepository.findByMatricula(matricula);
        return optServidor.orElseGet(() -> atualizaDadosNoSarh(matricula));
    }


    public List<Servidor> listar() {
        if (usuarioService.permissaoDiretor()) {
            log.info("listar por lotação do Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoService.getIdsLotacoes(servidorAtual.getLotacao().getId());
            return servidorRepository.listarPorLotacoes(idsLotacoes);
        }
        log.info("listarAll: Outros");
        return servidorRepository.listarTodos();
    }

    public List<Servidor> listar(Integer idLotacaoPai) {
        log.info("listar Por Lotação id: {}", idLotacaoPai);
        if (usuarioService.permissaoDiretor()) {
            log.info("listar por lotação do Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoService.getIdsLotacoes(servidorAtual.getLotacao().getId());
            return servidorRepository.listarPorLotacoes(idsLotacoes);
        }
        var idsLotacoes = lotacaoService.getIdsLotacoes(idLotacaoPai);
        return servidorRepository.listarPorLotacoes(idsLotacoes);
    }

    public List<Servidor> listar(String nome,
                                 Integer cracha,
                                 String matricula,
                                 Integer idLotacao) {
        if (usuarioService.permissaoDiretor()) {
            log.info("Paginar filtrado: Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoService.getIdsLotacoes(servidorAtual.getLotacao().getId());
            return servidorRepository.listarPorNomeOuCrachaOuMatriculaEeLotacoes(nome, cracha, matricula, idsLotacoes);
        }
        log.info("Paginar filtrado: Outros");
        var idsLotacoes = lotacaoService.getIdsLotacoes(idLotacao);
        return servidorRepository.listarPorNomeOuCrachaOuMatriculaEeLotacoes(nome, cracha, matricula, idsLotacoes);
    }


    public Page<Servidor> paginar(Pageable pageable) {
        if (usuarioService.permissaoDiretor()) {
            log.info("listar por lotação do Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoService.getIdsLotacoes(servidorAtual.getLotacao().getId());
            return servidorRepository.paginarPorLotacoes(idsLotacoes, pageable);
        }
        log.info("listarAll: Outros");
        return servidorRepository.findAll(pageable);
    }

    public Page<Servidor> paginar(String nome,
                                  Integer cracha,
                                  String matricula,
                                  Pageable pageable) {
        if (usuarioService.permissaoDiretor()) {
            log.info("Paginar filtrado: Diretor");
            var servidorAtual = servidorAtual();
            return servidorRepository.paginarPorNomeOuCrachaOuMatriculaEeIdLotacao(nome, cracha, matricula, servidorAtual.getLotacao().getId(), pageable);
        }
        log.info("Paginar filtrado: Outros");
        return servidorRepository.paginarPorNomeOuCrachaOuMatricula(nome, cracha, matricula, pageable);
    }

    public Servidor vinculaAusenciasServidorNoPeriodo(Servidor servidor, LocalDate dataInicio, LocalDate dataFim) {
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

    public Servidor buscaDiretorLotacao(Integer idLotacao) {
        //idLotacao IN (112,124, 136, 215) AND s.lotacao.id IN (115,127, 140, 218)
        if (idLotacao == 112 || idLotacao == 124 || idLotacao == 136 || idLotacao == 215) {
            var lotacoes = lotacaoService.getLotacoes(idLotacao);
            var lotacaoOptional = lotacoes.stream()
                    .filter(l -> l.getSigla().equals("SECVA"))
                    .findFirst();
            if (lotacaoOptional.isPresent()) {
                var lotacao = lotacaoOptional.get();
                idLotacao = lotacao.getId();
            }
        }
        Optional<Servidor> optServidor = servidorRepository.buscaDiretorLotacao(idLotacao);

        if (optServidor.isPresent()) {
            return optServidor.get();
        }
        throw new LotacaoNaoTemDiretorDireto(idLotacao);
    }
}
