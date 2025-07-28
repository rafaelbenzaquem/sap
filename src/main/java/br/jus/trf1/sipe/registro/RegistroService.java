package br.jus.trf1.sipe.registro;

import br.jus.trf1.sipe.alteracao.alteracao_registro.AlteracaoRegistroService;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracao;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracaoService;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.StatusPedido;
import br.jus.trf1.sipe.externo.coletor.historico.HistoricoExternalClient;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.registro.exceptions.RegistroInexistenteException;
import br.jus.trf1.sipe.servidor.Servidor;
import br.jus.trf1.sipe.usuario.UsuarioService;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioNaoAprovadorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static br.jus.trf1.sipe.comum.util.DataTempoUtil.paraString;

@Slf4j
@Service
public class RegistroService {

    private final UsuarioService usuarioService;
    private final HistoricoExternalClient historicoService;
    private final RegistroRepository registroRepository;
    private final PedidoAlteracaoService pedidoAlteracaoService;
    private final AlteracaoRegistroService alteracaoRegistroService;

    public RegistroService(UsuarioService usuarioService, HistoricoExternalClient historicoService,
                           RegistroRepository registroRepository, PedidoAlteracaoService pedidoAlteracaoService,
                           AlteracaoRegistroService alteracaoRegistroService) {
        this.usuarioService = usuarioService;
        this.historicoService = historicoService;
        this.registroRepository = registroRepository;
        this.pedidoAlteracaoService = pedidoAlteracaoService;
        this.alteracaoRegistroService = alteracaoRegistroService;
    }

    public Registro buscaRegistroPorId(Long id) {
        return registroRepository.findById(id).orElseThrow(() -> new RegistroInexistenteException(id));
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

        var matricula = ponto.getId().getMatricula();
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
        var matricula = ponto.getId().getMatricula();
        var dia = ponto.getId().getDia();

        var vinculo = usuarioService.buscaPorMatricula(matricula);

        var historicos = historicoService.buscarHistoricoDeAcesso(
                dia, null, vinculo.getCracha(), null, null);

        return historicos.stream().
                filter(historico ->
                        {
                            log.debug("historico {}", historico);
                            return registrosAtuais.stream().noneMatch(r ->
                                    {
                                        var filtered = Objects.equals(historico.acesso(), r.getCodigoAcesso());
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
                                .ponto(ponto)
                                .build()
                )
                .toList();
    }


    public Registro aprovarRegistro(Long idRegistro) {
        var registro = registroRepository.findById(idRegistro).orElseThrow(() -> new RegistroInexistenteException(idRegistro));
        var usuario = usuarioService.getUsuarioAtual();
        if (usuario instanceof Servidor servidor) {
            registro.setServidorAprovador(servidor);
            registro.setDataAprovacao(Timestamp.valueOf(LocalDateTime.now()));
            return registroRepository.save(registro);
        }
        throw new UsuarioNaoAprovadorException(usuario.getMatricula());
    }

    public List<Registro> addRegistros(Ponto ponto, List<Registro> registros) {

        var registrosNovos = registros.stream().
                map(registro -> addPonto(registro, ponto)).toList();

        return registroRepository.saveAll(registrosNovos);
    }

    public Registro addPonto(Registro registro, Ponto ponto) {
        return Registro.builder()
                .id(registro.getId())
                .hora(registro.getHora())
                .sentido(registro.getSentido().getCodigo())
                .servidorCriador(registro.getServidorCriador())
                .ativo(true)
                .codigoAcesso(registro.getCodigoAcesso() == null ? 0 : registro.getCodigoAcesso())
                .ponto(ponto)
                .build();
    }

    @Transactional
    public Registro atualizaRegistro(Ponto ponto, String justificativa, Registro registroAtualizado) {
        var usuarioAtual = usuarioService.getUsuarioAtual();

        registroAtualizado.setServidorCriador((Servidor) usuarioAtual);
        var id = registroAtualizado.getId();
        log.info("Atualiza registro {}", id);
        var opt = registroRepository.findById(id);
        if (opt.isPresent()) {
            var registro = opt.get();
            if (registro.getPonto().equals(ponto)) {
                registroAtualizado.setId(null);
                registroAtualizado.setPonto(ponto);
                registroAtualizado = registroRepository.save(registroAtualizado);
                registro.setRegistroNovo(registroAtualizado);
                registroRepository.save(registro);
                return registroAtualizado;
            }
            throw new IllegalArgumentException("Registro não pertence ao ponto: " + ponto.getId());
        }
        throw new RegistroInexistenteException(id);
    }

    public Registro apagar(Long id) {
        registroRepository.apagarRegistroPorId(id);
//        var opt = registroRepository.findById(id);
//        if (opt.isPresent()) {
//            var registro = opt.get();
//            log.info("apagando registro {}", id);
//            registroRepository.delete(registro);
//            return registro;
//        }
//        throw new RegistroInexistenteException(id);
        return Registro.builder().build();
    }
}
