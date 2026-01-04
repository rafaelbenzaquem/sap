package br.jus.trf1.sipe.registro.domain.service;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.Acao;
import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.port.in.AlteracaoRegistroServicePort;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
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

    private final AlteracaoRegistroServicePort alteracaoRegistroServicePort;

    public RegistroServiceAdapter(UsuarioServicePort usuarioServicePort,
                                  RegistroExternoPort registroExternoPort,
                                  RegistroPersistencePort registroPersistencePort,
                                  AlteracaoRegistroServicePort alteracaoRegistroServicePort) {
        this.usuarioServicePort = usuarioServicePort;
        this.registroExternoPort = registroExternoPort;
        this.registroPersistencePort = registroPersistencePort;
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
    public List<Registro> listaAtuaisDoPonto(String matricula, LocalDate dia, boolean apenasAtivos) {
        return registroPersistencePort.listaAtuaisDoPonto(matricula, dia, apenasAtivos);
    }

    @Override
    public List<Registro> salvaNovosDeSistemaExternoEmBaseInterna(Ponto ponto) {

        var matricula = ponto.getId().getUsuario().getMatricula();
        var dia = ponto.getId().getDia();

        var novosRegistros = buscaNovosEmSistemaExterno(matricula, dia).
                stream().peek(registro -> registro.setPonto(ponto)).toList();

        return registroPersistencePort.salvaTodos(novosRegistros);

//        return registroPersistencePort.listaAtuaisDoPonto(matricula, dia, false);

    }

    @Override
    public List<Registro> buscaNovosEmSistemaExterno(String matricula, LocalDate dia) {
        var registrosAtuais = registroPersistencePort.listaRegistrosProvenientesDoSistemaExternoPorPonto(matricula, dia);
        return filtraNovosRegistros(matricula, dia, registrosAtuais);
    }


    private List<Registro> filtraNovosRegistros(String matricula, LocalDate dia, List<Registro> registrosAtuais) {
        var cracha = usuarioServicePort.buscaPorMatricula(matricula).getCracha();
        var registros = registroExternoPort.buscaRegistrosDoDiaPorCracha(dia, cracha);

        if (registrosAtuais.isEmpty())
            return registros;

        return registros.stream().
                filter(registro ->
                        {
                            log.debug("Historico de registros {}", registro);
                            return registrosAtuais.stream().noneMatch(r ->
                                    {
                                        var filtered = Objects.equals(registro.getCodigoAcesso(), r.getCodigoAcesso());
                                        log.debug("registro {} - {}",
                                                !filtered ? "foi filtrado" : "não foi filtrado", r);
                                        return filtered;
                                    }
                            );
                        }

                ).toList();
    }



    @Override
    public Registro aprova(Long idRegistro) {
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
    public List<Registro> salva(PedidoAlteracao pedidoAlteracao, Ponto ponto, List<Registro> registros) {
        var usuarioAutenticado = usuarioServicePort.getUsuarioAutenticado();
//        usuarioServicePort.temPermissaoRecurso(ponto); TODO criar um interceptador que valida a segurança
        var registrosNovos = registros.stream().
                peek(registro -> {
                            registro.setPonto(ponto);
                            registro.setServidorCriador((Servidor) usuarioAutenticado);
                        }
                ).toList();
        registrosNovos = registroPersistencePort.salvaTodos(registrosNovos);

        registrosNovos.forEach(registroNovo ->
                alteracaoRegistroServicePort.salvarAlteracaoNoRegistroDePonto(pedidoAlteracao.getId(),
                        null,
                        registroNovo.getId(),
                        Acao.CRIAR));

        return registrosNovos;
    }


    @Override
    public void remove(PedidoAlteracao pedidoAlteracao, Ponto ponto, Registro registro) {
        usuarioServicePort.temPermissaoRecurso(ponto);

        var alteracaoRegistroOpt = pedidoAlteracao.getAlteracaoRegistros().stream().
                filter(pa -> registro.equals(pa.getRegistroOriginal()) || registro.equals(pa.getRegistroNovo())).findFirst();

        alteracaoRegistroOpt.ifPresent(alteracaoRegistro -> alteracaoRegistroServicePort.apagar(alteracaoRegistro.getId()));
    }


    @Override
    public Registro atualiza(PedidoAlteracao pedidoAlteracao, Ponto ponto, Registro registroAtualizado) {
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
        throw new IllegalArgumentException("Registro não pertence ao ponto: " + ponto.getId());
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
