package br.jus.trf1.sipe.registro;

import br.jus.trf1.sipe.externo.coletor.historico.HistoricoService;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.registro.exceptions.RegistroInexistenteException;
import br.jus.trf1.sipe.usuario.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static br.jus.trf1.sipe.comum.util.DataTempoUtil.paraString;

@Slf4j
@Service
public class RegistroService {

    private final UsuarioService usuarioService;
    private final HistoricoService historicoService;
    private final RegistroRepository registroRepository;


    public RegistroService(UsuarioService usuarioService,
                           HistoricoService historicoService,
                           RegistroRepository registroRepository) {
        this.usuarioService = usuarioService;
        this.historicoService = historicoService;
        this.registroRepository = registroRepository;
    }

    public Registro buscaRegistroPorId(Long id) {
        return registroRepository.findById(id).orElseThrow(() -> new RegistroInexistenteException(id));
    }

    public Boolean existe(Long id) {
        return registroRepository.existsById(id);
    }


    public List<Registro> listarRegistrosPonto(String matricula, LocalDate dia) {
        return registroRepository.listarRegistrosAtuaisAtivosDoPonto(matricula, dia);
    }

    public List<Registro> atualizaRegistrosNovos(Ponto ponto) {

        var matricula = ponto.getId().getMatricula();
        var dia = ponto.getId().getDia();

        var registrosAtuais = registroRepository.listarRegistrosHistoricosDoPonto(matricula, dia);

        var registros = filtraNovosRegistros(ponto, registrosAtuais);

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
                .ativo(true)
                .codigoAcesso(registro.getCodigoAcesso())
                .ponto(ponto)
                .build();
    }

    public Registro atualizaRegistro(Ponto ponto, Registro registroAtualizado) {
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
        log.info("apagando registro {}", id);
        var opt = registroRepository.findById(id);
        if (opt.isPresent()) {
            var registro = opt.get();
            registroRepository.deleteById(id);
            return registro;
        }
        throw new RegistroInexistenteException(id);
    }
}
