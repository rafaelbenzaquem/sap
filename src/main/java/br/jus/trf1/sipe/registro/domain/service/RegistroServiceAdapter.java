package br.jus.trf1.sipe.registro.domain.service;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.Acao;
import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.port.in.AlteracaoRegistroServicePort;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.registro.domain.model.Registro;
import br.jus.trf1.sipe.registro.domain.port.in.RegistroServicePort;
import br.jus.trf1.sipe.registro.domain.port.out.RegistroExternoPort;
import br.jus.trf1.sipe.registro.domain.port.out.RegistroPersistencePort;
import br.jus.trf1.sipe.servidor.domain.port.in.ServidorServicePort;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioNaoAutorizadoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class RegistroServiceAdapter implements RegistroServicePort {

    private final ServidorServicePort servidorServicePort;

    private final RegistroExternoPort registroExternoPort;
    private final RegistroPersistencePort registroPersistencePort;
    private final AlteracaoRegistroServicePort alteracaoRegistroServicePort;

    public RegistroServiceAdapter(ServidorServicePort servidorServicePort,
                                  RegistroExternoPort registroExternoPort,
                                  RegistroPersistencePort registroPersistencePort,
                                  AlteracaoRegistroServicePort alteracaoRegistroServicePort) {
        this.servidorServicePort = servidorServicePort;
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
        var cracha = servidorServicePort.buscaPorMatricula(matricula).getCracha();
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
        var servidorAtual = servidorServicePort.servidorAtual();
        registro.setServidorAprovador(servidorAtual);
        registro.setDataAprovacao(Timestamp.valueOf(LocalDateTime.now()));
        return registroPersistencePort.salva(registro);
    }


    @Override
    public List<Registro> salva(PedidoAlteracao pedidoAlteracao, Ponto ponto, List<Registro> registros) {
        var servidorAtual = servidorServicePort.servidorAtual();
//        usuarioServicePort.temPermissaoRecurso(ponto); TODO criar um interceptador que valida a segurança
        var registrosNovos = registros.stream().
                peek(registro -> {
                            registro.setPonto(ponto);
                            registro.setServidorCriador(servidorAtual);
                        }
                ).toList();

        registrosNovos.forEach(registroNovo ->
                alteracaoRegistroServicePort.salvaAlteracaoNoRegistroDePonto(pedidoAlteracao,
                        null,
                        registroNovo,
                        Acao.CRIAR));
        return registrosNovos;
    }


    @Override
    public void remove(PedidoAlteracao pedidoAlteracao, Ponto ponto, Registro registro) {
//        usuarioServicePort.temPermissaoRecurso(ponto);

        var alteracaoRegistroOpt = pedidoAlteracao.getAlteracoesRegistros().stream().
                filter(pa -> registro.equals(pa.getRegistroOriginal()) || registro.equals(pa.getRegistroNovo())).findFirst();

        alteracaoRegistroOpt.ifPresent(alteracaoRegistro -> alteracaoRegistroServicePort.apagar(alteracaoRegistro.getId()));
    }


    @Override
    public Registro atualiza(PedidoAlteracao pedidoAlteracao, Ponto ponto, Registro registroAtualizado) {
        var servidorAtual = servidorServicePort.servidorAtual();
//TODO rever uso        usuarioServicePort.temPermissaoRecurso(ponto);
        registroAtualizado.setServidorCriador(servidorAtual);
        var id = registroAtualizado.getId();
        log.info("Atualiza registro {}", id);
        var registroAtual = registroPersistencePort.buscaPorId(id);

        if (registroAtual.getPonto().equals(ponto)) {
            registroAtualizado.setId(null);
            registroAtualizado.setPonto(ponto);
            var alteracaoRegistro = alteracaoRegistroServicePort.salvaAlteracaoNoRegistroDePonto(pedidoAlteracao, registroAtual, registroAtualizado, Acao.ALTERAR);
            return alteracaoRegistro.getRegistroNovo();
        }
        throw new IllegalArgumentException("Registro não pertence ao ponto: " + ponto.getId());
    }

    @Override
    public Registro apaga(Long idRegistro) {
        var servidorAtual = servidorServicePort.servidorAtual();
        var registro = buscaRegistroPorId(idRegistro);
        var ponto = registro.getPonto();
        if (registro.getServidorAprovador() == null || registro.getServidorAprovador().equals(servidorAtual)) {
//TODO            usuarioServicePort.temPermissaoRecurso(ponto);
            registroPersistencePort.apagarPorId(idRegistro);
            return registro;
        }
        throw new UsuarioNaoAutorizadoException("Usuário %s não autorizado a manipular o recurso".formatted(servidorAtual.getMatricula()));
    }


}
