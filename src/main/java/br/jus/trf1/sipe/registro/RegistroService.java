package br.jus.trf1.sipe.registro;

import br.jus.trf1.sipe.alteracao.alteracao_registro.Acao;
import br.jus.trf1.sipe.alteracao.alteracao_registro.AlteracaoRegistroService;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracao;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracaoService;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.ponto.infrastructure.jpa.PontoJpa;
import br.jus.trf1.sipe.ponto.infrastructure.jpa.PontoJpaMapper;
import br.jus.trf1.sipe.registro.exceptions.RegistroInexistenteException;
import br.jus.trf1.sipe.registro.externo.coletor.RegistroExternalService;
import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import br.jus.trf1.sipe.servidor.infrastructure.jpa.ServidorJpa;
import br.jus.trf1.sipe.servidor.infrastructure.jpa.ServidorJpaMapper;
import br.jus.trf1.sipe.usuario.domain.service.UsuarioServiceAdapter;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioNaoAprovadorException;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioNaoAutorizadoException;
import br.jus.trf1.sipe.usuario.infrastructure.jpa.UsuarioJpaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class RegistroService {

    private final UsuarioServiceAdapter usuarioService;
    private final RegistroExternalService registroExternalService;
    private final RegistroRepository registroRepository;
    private final PedidoAlteracaoService pedidoAlteracaoService;
    private final AlteracaoRegistroService alteracaoRegistroService;

    public RegistroService(UsuarioServiceAdapter usuarioService, RegistroExternalService registroExternalService,
                           RegistroRepository registroRepository, PedidoAlteracaoService pedidoAlteracaoService,
                           AlteracaoRegistroService alteracaoRegistroService) {
        this.usuarioService = usuarioService;
        this.registroExternalService = registroExternalService;
        this.registroRepository = registroRepository;
        this.pedidoAlteracaoService = pedidoAlteracaoService;
        this.alteracaoRegistroService = alteracaoRegistroService;
    }

    public Registro buscaRegistroPorId(Long id) {
        return registroRepository.buscaRegistroPorId(id).orElseThrow(() -> new RegistroInexistenteException(id));
    }

    public Boolean existe(Long id) {
        return registroRepository.existsById(id);
    }


    public List<Registro> listarRegistrosPonto(String matricula, LocalDate dia, boolean todos) {
        if (todos) {
            return registroRepository.listarRegistrosAtuaisDoPonto(matricula, dia);
        }
        return registroRepository.listarRegistrosAtuaisAtivosDoPonto(matricula, dia);
    }

    public List<Registro> atualizaRegistrosSistemaDeAcesso(Ponto ponto) {

        var matricula = ponto.getId().getUsuario().getMatricula();
        var dia = ponto.getId().getDia();

        var registrosAtuais = registroRepository.listarRegistrosHistoricosDoPonto(matricula, dia);

        var registros = filtraNovosRegistros(ponto, registrosAtuais);
        if (registrosAtuais.isEmpty()) {
            return registroRepository.saveAll(registros);
        }
        registroRepository.saveAll(registros);

        return registroRepository.listarRegistrosAtuaisAtivosDoPonto(matricula, dia);

    }

    private List<Registro> filtraNovosRegistros(Ponto ponto, List<Registro> registrosAtuais) {
        var matricula = ponto.getId().getUsuario().getMatricula();
        var dia = ponto.getId().getDia();

        var vinculo = usuarioService.buscaPorMatricula(matricula);

        var registros = registroExternalService.buscaRegistrosDoDiaPorCracha(dia, vinculo.getCracha());

        return registros.stream().
                filter(registroExternal ->
                        {
                            log.debug("Historico de registros {}", registroExternal);
                            return registrosAtuais.stream().noneMatch(r ->
                                    {
                                        var filtered = Objects.equals(registroExternal.acesso(), r.getCodigoAcesso());
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
                                .ativo(true)
                                .ponto(PontoJpaMapper.toEntity(ponto))
                                .build()
                )
                .toList();
    }


    public Registro aprovarRegistro(Long idRegistro) {
        var registro = registroRepository.findById(idRegistro).orElseThrow(() -> new RegistroInexistenteException(idRegistro));
        var usuario = usuarioService.getUsuarioAutenticado();
        if (usuario instanceof Servidor servidor) {
            var servidorJpa = ServidorJpaMapper.toEntity(servidor);
            registro.setServidorAprovador(servidorJpa);
            registro.setDataAprovacao(Timestamp.valueOf(LocalDateTime.now()));
            return registroRepository.save(registro);
        }
        throw new UsuarioNaoAprovadorException(usuario.getMatricula());
    }

    @Transactional
    public List<Registro> addRegistros(PedidoAlteracao pedidoAlteracao, Ponto ponto, List<Registro> registros) {
        var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        usuarioService.temPermissaoRecurso(ponto);
        var usuarioJpa = UsuarioJpaMapper.toEntity(usuarioAutenticado);
        var registrosNovos = registros.stream().
                map(registro -> addPontoCriador(registro, PontoJpaMapper.toEntity(ponto), (ServidorJpa) usuarioJpa)).toList();
        registrosNovos = registroRepository.saveAll(registrosNovos);

        registrosNovos.forEach(registroNovo -> {
            alteracaoRegistroService.salvarAlteracaoNoRegistroDePonto(pedidoAlteracao.getId(), null, registroNovo.getId(), Acao.CRIAR);
        });

        return registrosNovos;
    }


    @Transactional
    public void removeRegistro(PedidoAlteracao pedidoAlteracao, PontoJpa pontoJpa, Registro registro) {
        usuarioService.temPermissaoRecurso(pontoJpa);

        var alteracaoRegistroOpt = pedidoAlteracao.getAlteracaoRegistros().stream().
                filter(pa -> registro.equals(pa.getRegistroOriginal()) || registro.equals(pa.getRegistroNovo())).findFirst();

        alteracaoRegistroOpt.ifPresent(alteracaoRegistro -> alteracaoRegistroService.apagar(alteracaoRegistro.getId()));


    }

    public Registro addPontoCriador(Registro registro, PontoJpa pontoJpa, ServidorJpa servidor) {
        return Registro.builder()
                .id(registro.getId())
                .hora(registro.getHora())
                .sentido(registro.getSentido().getCodigo())
                .servidorCriador(servidor)
                .ativo(true)
                .codigoAcesso(registro.getCodigoAcesso() == null ? 0 : registro.getCodigoAcesso())
                .ponto(pontoJpa)
                .build();
    }

    @Transactional
    public Registro atualizaRegistro(PedidoAlteracao pedidoAlteracao, Ponto ponto, Registro registroAtualizado) {
        var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        var usuarioJpa = UsuarioJpaMapper.toEntity(usuarioAutenticado);
        usuarioService.temPermissaoRecurso(ponto);
        registroAtualizado.setServidorCriador((ServidorJpa) usuarioJpa);
        var id = registroAtualizado.getId();
        log.info("Atualiza registro {}", id);
        var opt = registroRepository.findById(id);

        if (opt.isPresent()) {
            var registro = opt.get();
            if (registro.getPonto().equals(ponto)) {
                registroAtualizado.setId(null);
                registroAtualizado.setPonto(PontoJpaMapper.toEntity(ponto));
                registroAtualizado = registroRepository.save(registroAtualizado);
                registro.setRegistroNovo(registroAtualizado);
                registroRepository.save(registro);

                alteracaoRegistroService.salvarAlteracaoNoRegistroDePonto(pedidoAlteracao.getId(), registro.getId(), registroAtualizado.getId(), Acao.ALTERAR);

                return registroAtualizado;
            }
            throw new IllegalArgumentException("Registro não pertence ao pontoJpa: " + ponto.getId());
        }
        throw new RegistroInexistenteException(id);
    }

    @Transactional
    public Registro apagar(Long idRegistro, Long idPedidoAlteracao) {

        var usuarioAtual = usuarioService.getUsuarioAutenticado();
        var registro = buscaRegistroPorId(idRegistro);
        var ponto = registro.getPonto();
        if (registro.getServidorAprovador() == null || registro.getServidorAprovador().equals(usuarioAtual)) {

            usuarioService.temPermissaoRecurso(ponto);
            var pedidoAlteracao = pedidoAlteracaoService.buscaPedidoAlteracao(idPedidoAlteracao);

            var alteracaoRegistroOptional = pedidoAlteracao.getAlteracaoRegistros().
                    stream().filter(ar -> registro.equals(ar.getRegistroOriginal()) || registro.equals(ar.getRegistroNovo()))
                    .findFirst();
            alteracaoRegistroOptional.ifPresent(alteracaoRegistro -> {
                alteracaoRegistroService.apagar(alteracaoRegistro.getId());
            });
            registroRepository.apagarRegistroPorId(idRegistro);

            return registro;
        }
        throw new UsuarioNaoAutorizadoException("Usuário %s não autorizado a manipular o recurso".formatted(usuarioAtual.getMatricula()));
    }
}
