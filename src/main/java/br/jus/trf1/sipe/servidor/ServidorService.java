package br.jus.trf1.sipe.servidor;

import br.jus.trf1.sipe.ausencia.AusenciaRepository;
import br.jus.trf1.sipe.externo.jsarh.ausencias.AusenciaExternaService;
import br.jus.trf1.sipe.externo.jsarh.servidor.ServidorExternoService;
import br.jus.trf1.sipe.lotacao.LotacaoService;
import br.jus.trf1.sipe.servidor.exceptions.ServidorInexistenteException;
import br.jus.trf1.sipe.usuario.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
                           AusenciaRepository ausenciaRepository,LotacaoService lotacaoService) {
        this.usuarioService = usuarioService;
        this.servidorRepository = servidorRepository;
        this.servidorExternoService = servidorExternoService;
        this.ausenciaExternaService = ausenciaExternaService;
        this.ausenciaRepository = ausenciaRepository;
        this.lotacaoService = lotacaoService;
    }

    public Servidor vinculaUsuarioServidor(String matricula) {
        log.info("Buscando usuário com matricula: {}", matricula);
        var usuario = usuarioService.buscaPorMatricula(matricula);
        var servidorExterno = servidorExternoService.buscaServidorExterno(matricula);

        var servidor = toModel(usuario, servidorExterno);

        return servidorRepository.save(servidor);
    }

    public Servidor servidorAtual() {
        var usuarioAtual = usuarioService.getUsuarioAtual();
        var servidorAtualOpt = servidorRepository.findById(usuarioAtual.getId());
        if (servidorAtualOpt.isPresent()) {
            return servidorAtualOpt.get();
        }
        throw new ServidorInexistenteException(usuarioAtual.getId());
    }


    public Page<Servidor> listar(Pageable pageable) {
        if (usuarioService.permissaoDiretor()) {
            log.info("listarAll: Diretor");
            var servidorAtual = servidorAtual();
            var idsLotacoes = lotacaoService.getLotacaos(servidorAtual.getLotacao().getId());
            return servidorRepository.buscarPorLotacoes(idsLotacoes, pageable);
        }
        log.info("listarAll: Outros");
        return servidorRepository.findAll(pageable);
    }



    public Page<Servidor> buscaPorNomeOuCrachaOuMatricula(String nome,
                                                          Integer cracha,
                                                          String matricula,
                                                          Pageable pageable) {
        if (usuarioService.permissaoDiretor()) {
            log.info("listar filtrado: Diretor");
            var servidorAtual = servidorAtual();
            return servidorRepository.findAllByNomeOrCrachaOrMatriculaAndIdLotacao(nome, cracha, matricula,servidorAtual.getLotacao().getId(), pageable);
        }
        log.info("listar filtrado: Outros");
        return servidorRepository.findAllByNomeOrCrachaOrMatricula(nome, cracha, matricula, pageable);
    }

    public Servidor buscaPorMatricula(String matricula) {
        Optional<Servidor> optServidor = servidorRepository.findByMatricula(matricula);
        return optServidor.orElseGet(() -> vinculaUsuarioServidor(matricula));
    }

    public Servidor vinculaAusenciasServidorNoPeriodo(Servidor servidor, LocalDate dataInicio, LocalDate dataFim) {
        log.info("Vinculando ausencias do servidor {}", servidor);

        var ausenciasExternas = ausenciaExternaService.
                buscaAusenciasServidorPorPeriodo(servidor.getMatricula(), dataInicio, dataFim);

        var ausenciasExistentes = ausenciaRepository.listaAusenciasPorServidorMaisPeriodo(servidor, dataInicio, dataFim);

        var novasAusencias = ausenciasExternas.stream().map(auEx -> auEx.toModel(servidor)).toList();

        var ausenciasParaDelete = ausenciasExistentes.stream().filter(ae -> !novasAusencias.contains(ae)).toList();
        var ausenciasParaSalve = novasAusencias.stream().filter(ae -> !ausenciasExistentes.contains(ae)).toList();

        ausenciaRepository.deleteAll(ausenciasParaDelete);

        ausenciaRepository.saveAll(ausenciasParaSalve);

        var todasAusenciasDoPeriodo = ausenciaRepository.listaAusenciasPorServidorMaisPeriodo(servidor, dataInicio, dataFim);
        servidor.setAusencias(new ArrayList<>(todasAusenciasDoPeriodo));
        return servidor;
    }

}
