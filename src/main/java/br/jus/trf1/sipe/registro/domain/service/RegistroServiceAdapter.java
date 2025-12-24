package br.jus.trf1.sipe.registro.domain.service;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.Acao;
import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.port.in.AlteracaoRegistroServicePort;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.port.in.PedidoAlteracaoServicePort;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.registro.domain.model.Registro;
import br.jus.trf1.sipe.registro.domain.port.in.RegistroServicePort;
import br.jus.trf1.sipe.registro.domain.port.out.RegistroExternoPort;
import br.jus.trf1.sipe.registro.domain.port.out.RegistroPersistencePort;
import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import br.jus.trf1.sipe.usuario.domain.port.in.UsuarioServicePort;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioNaoAprovadorException;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioNaoAutorizadoException;
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
public class RegistroServiceAdapter implements RegistroServicePort {

    private final UsuarioServicePort usuarioServicePort;

    private final RegistroExternoPort registroExternoPort;
    private final RegistroPersistencePort registroPersistencePort;

    private final PedidoAlteracaoServicePort pedidoAlteracaoServicePort;
    private final AlteracaoRegistroServicePort alteracaoRegistroServicePort;

    public RegistroServiceAdapter(UsuarioServicePort usuarioServicePort,
                                  RegistroExternoPort registroExternoPort,
                                  RegistroPersistencePort registroPersistencePort,
                                  PedidoAlteracaoServicePort pedidoAlteracaoServicePort,
                                  AlteracaoRegistroServicePort alteracaoRegistroServicePort) {
        this.usuarioServicePort = usuarioServicePort;
        this.registroExternoPort = registroExternoPort;
        this.registroPersistencePort = registroPersistencePort;
        this.pedidoAlteracaoServicePort = pedidoAlteracaoServicePort;
        this.alteracaoRegistroServicePort = alteracaoRegistroServicePort;
    }

    @Override
    public Registro buscaRegistroPorId(Long id) {
        return registroPersistencePort.buscaPorId(id);
    }

    @Override
    public Boolean existe(Long id) {
        return registroPersistencePort.existe(id);
    }


    @Override
    public List<Registro> listarRegistrosAtivosPonto(String matricula, LocalDate dia, boolean apenasAtivos) {
        return registroPersistencePort.listaAtuaisDoPonto(matricula, dia, apenasAtivos);
    }

    @Override
    public List<Registro> atualizaRegistrosSistemaDeAcesso(Ponto ponto) {

        var matricula = ponto.getId().getUsuario().getMatricula();
        var dia = ponto.getId().getDia();

        var registrosAtuais = registroPersistencePort.listaRegistrosProvenientesDoSistemaExternoPorPonto(matricula, dia);

        var novosRegistros = filtraNovosRegistros(ponto, registrosAtuais);

        registroPersistencePort.salvaTodos(novosRegistros);

        return registroPersistencePort.listaAtuaisDoPonto(matricula, dia, false);

    }

    private List<Registro> filtraNovosRegistros(Ponto ponto, List<Registro> registrosAtuais) {
        var matricula = ponto.getId().getUsuario().getMatricula();
        var dia = ponto.getId().getDia();

        var vinculo = usuarioServicePort.buscaPorMatricula(matricula);

        var registros = registroExternoPort.buscaRegistrosDoDiaPorCracha(dia, vinculo.getCracha());

        return registros.stream().
                filter(registroExternal ->
                        {
                            log.debug("Historico de registros {}", registroExternal);
                            return registrosAtuais.stream().noneMatch(r ->
                                    {
                                        var filtered = Objects.equals(registroExternal.getCodigoAcesso(), r.getCodigoAcesso());
                                        log.debug("registro {} - {}",
                                                !filtered ? "foi filtrado" : "não foi filtrado", r);
                                        return filtered;
                                    }
                            );
                        }

                ).toList();
    }


    @Override
    public Registro aprovarRegistro(Long idRegistro) {
        var registro = registroPersistencePort.buscaPorId(idRegistro);
        var usuario = usuarioServicePort.getUsuarioAutenticado();
        if (usuario instanceof Servidor servidor) {

            registro.setServidorAprovador(servidor);
            registro.setDataAprovacao(Timestamp.valueOf(LocalDateTime.now()));
            return registroPersistencePort.salvar(registro);
        }
        throw new UsuarioNaoAprovadorException(usuario.getMatricula());
    }


    @Override
    public List<Registro> addRegistros(PedidoAlteracao pedidoAlteracao, Ponto ponto, List<Registro> registros) {
        var usuarioAutenticado = usuarioServicePort.getUsuarioAutenticado();
        usuarioServicePort.temPermissaoRecurso(ponto);
        var registrosNovos = registros.stream().
                map(registro -> addPontoCriador(registro, ponto, (Servidor) usuarioAutenticado)).toList();
        registrosNovos = registroPersistencePort.salvaTodos(registrosNovos);

        registrosNovos.forEach(registroNovo -> {
            alteracaoRegistroServicePort.salvarAlteracaoNoRegistroDePonto(pedidoAlteracao.getId(), null, registroNovo.getId(), Acao.CRIAR);
        });

        return registrosNovos;
    }


    @Transactional
    @Override
    public void removeRegistro(PedidoAlteracao pedidoAlteracao, Ponto ponto, Registro registro) {
        usuarioServicePort.temPermissaoRecurso(ponto);

        var alteracaoRegistroOpt = pedidoAlteracao.getAlteracaoRegistros().stream().
                filter(pa -> registro.equals(pa.getRegistroOriginal()) || registro.equals(pa.getRegistroNovo())).findFirst();

        alteracaoRegistroOpt.ifPresent(alteracaoRegistro -> alteracaoRegistroServicePort.apagar(alteracaoRegistro.getId()));


    }

    @Override
    public Registro addPontoCriador(Registro registro, Ponto ponto, Servidor servidor) {
        return Registro.builder()
                .id(registro.getId())
                .hora(registro.getHora())
                .sentido(registro.getSentido())
                .servidorCriador(servidor)
                .ativo(true)
                .codigoAcesso(registro.getCodigoAcesso() == null ? 0 : registro.getCodigoAcesso())
                .ponto(ponto)
                .build();
    }

    @Override
    public Registro atualizaRegistro(PedidoAlteracao pedidoAlteracao, Ponto ponto, Registro registroAtualizado) {
        var usuarioAutenticado = usuarioServicePort.getUsuarioAutenticado();
        usuarioServicePort.temPermissaoRecurso(ponto);
        registroAtualizado.setServidorCriador((Servidor) usuarioAutenticado);
        var id = registroAtualizado.getId();
        log.info("Atualiza registro {}", id);
        var registro = registroPersistencePort.buscaPorId(id);

        if (registro.getPonto().equals(ponto)) {
            registroAtualizado.setId(null);
            registroAtualizado.setPonto(ponto);
            registroAtualizado = registroPersistencePort.salvar(registroAtualizado);
            registro.setRegistroNovo(registroAtualizado);
            registroPersistencePort.salvar(registro);

            alteracaoRegistroServicePort.salvarAlteracaoNoRegistroDePonto(pedidoAlteracao.getId(), registro.getId(), registroAtualizado.getId(), Acao.ALTERAR);

            return registroAtualizado;
        }
        throw new IllegalArgumentException("RegistroJpa não pertence ao pontoJpa: " + ponto.getId());
    }

    @Override
    public Registro apaga(Long idRegistro) {

        var usuarioAtual = usuarioServicePort.getUsuarioAutenticado();
        var registro = buscaRegistroPorId(idRegistro);
        var ponto = registro.getPonto();
        if (registro.getServidorAprovador() == null || registro.getServidorAprovador().equals(usuarioAtual)) {

            usuarioServicePort.temPermissaoRecurso(ponto);

            registroPersistencePort.apagarPorId(idRegistro);
            return registro;
        }
        throw new UsuarioNaoAutorizadoException("Usuário %s não autorizado a manipular o recurso".formatted(usuarioAtual.getMatricula()));
    }


}
