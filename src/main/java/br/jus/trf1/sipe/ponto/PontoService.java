package br.jus.trf1.sipe.ponto;

import br.jus.trf1.sipe.externo.jsarh.ausencias.AusenciaExterna;
import br.jus.trf1.sipe.externo.jsarh.ausencias.AusenciaExternaService;
import br.jus.trf1.sipe.externo.jsarh.feriado.FeriadoExternalClient;
import br.jus.trf1.sipe.externo.jsarh.feriado.dto.FeriadoExternalResponse;
import br.jus.trf1.sipe.ponto.exceptions.PontoExistenteException;
import br.jus.trf1.sipe.ponto.exceptions.PontoInexistenteException;
import br.jus.trf1.sipe.registro.RegistroService;
import br.jus.trf1.sipe.usuario.UsuarioAtualService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

import static br.jus.trf1.sipe.comum.util.DataTempoUtil.*;

/**
 * Serviço para gerenciamento de pontos de registro de servidores.
 *
 * <p>Fornece operações de criação, atualização, busca e carregamento de pontos,
 * integrando-se com serviços externos de ausências e feriados e atualizando
 * registros conforme regras de negócio.</p>
 */
@Slf4j
@Service
public class PontoService {

    private final PontoRepository pontoRepository;
    private final RegistroService registroService;
    private final AusenciaExternaService ausenciaService;
    private final FeriadoExternalClient feriadoService;
    private final UsuarioAtualService usuarioAtualService;

    public PontoService(PontoRepository pontoRepository,
                        RegistroService registroService,
                        AusenciaExternaService ausenciaService,
                        FeriadoExternalClient feriadoService,
                        UsuarioAtualService usuarioAtualService) {
        this.pontoRepository = pontoRepository;
        this.registroService = registroService;
        this.ausenciaService = ausenciaService;
        this.feriadoService = feriadoService;
        this.usuarioAtualService = usuarioAtualService;
    }

    /**
     * Verifica a existência de um ponto para a matrícula e dia informados.
     *
     * @param matricula matrícula do servidor
     * @param dia       dia do ponto a verificar
     * @return {@code true} se o ponto existir, {@code false} caso contrário
     */
    public boolean existe(String matricula, LocalDate dia) {
        return pontoRepository.existsById(PontoId.builder().
                matricula(matricula).
                dia(dia).
                build());
    }

    /**
     * Busca um ponto específico por matrícula e dia.
     *
     * @param matricula matrícula do servidor
     * @param dia       data do ponto a buscar
     * @return ponto encontrado
     * @throws PontoInexistenteException se não existir ponto para os parâmetros
     */
    public Ponto buscaPonto(String matricula, LocalDate dia) {
        log.info("Buscando Ponto - {} - {} ", paraString(dia), matricula);
        usuarioAtualService.permissoesNivelUsuario(matricula);
        var pontoOpt = pontoRepository.findById(PontoId.builder().
                matricula(matricula).
                dia(dia).
                build()
        );
        return pontoOpt.orElseThrow(
                () -> new PontoInexistenteException(matricula, dia)
        );
    }


    /**
     * Retorna lista de pontos em um intervalo de datas para a matrícula informada.
     *
     * @param matricula matrícula do servidor
     * @param inicio    data de início do período
     * @param fim       data de fim do período
     * @return lista de pontos no período
     */
    public List<Ponto> buscarPontos(String matricula, LocalDate inicio, LocalDate fim) {
        log.info("Lista de Pontos - {} - {} - {} ", paraString(inicio), paraString(fim), matricula);
        usuarioAtualService.permissoesNivelUsuario(matricula);
        return pontoRepository.buscaPontosPorPeriodo(matricula, inicio, fim);
    }

    /**
     * Monta a descrição do ponto incluindo dia da semana, eventual ausência e feriado.
     *
     * @param dia      data do ponto
     * @param ausencia optional com dados de ausência externa
     * @param feriado  optional com dados de feriado externo
     * @return descrição completa formatada
     */
    private String defineDescricao(LocalDate dia, Optional<AusenciaExterna> ausencia, Optional<FeriadoExternalResponse> feriado) {
        return dia.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.of("pt", "BR")) + "\n" +
                ausencia.map(a -> ", " + a.getDescricao()).orElse("") + "\n" +
                feriado.map(f -> ", " + f.getDescricao()).orElse("");
    }

    /**
     * Define o índice do ponto conforme dia, ausência ou feriado.
     *
     * @param dia      data do ponto
     * @param ausencia optional com dados de ausência externa
     * @param feriado  optional com dados de feriado externo
     * @return índice do ponto (AUSENCIA, DOMINGO_E_FERIADOS, SABADO ou DIA_UTIL)
     */
    private IndicePonto defineIndice(LocalDate dia, Optional<AusenciaExterna> ausencia, Optional<FeriadoExternalResponse> feriado) {
        return ausencia.map(a -> IndicePonto.AUSENCIA).
                orElseGet(() -> feriado.map(f -> IndicePonto.DOMINGO_E_FERIADOS).
                        orElse(dia.getDayOfWeek().getValue() == 7 ? IndicePonto.DOMINGO_E_FERIADOS :
                                dia.getDayOfWeek().getValue() == 6 ? IndicePonto.SABADO :
                                        IndicePonto.DIA_UTIL)
                );
    }

    /**
     * Cria um novo ponto, calculando descrição e índice conforme regras de negócio,
     * persiste no repositório e atualiza registros associados.
     *
     * @param ponto objeto a ser criado
     * @return ponto salvo
     * @throws PontoExistenteException se já existir ponto para matrícula e dia informados
     */
    @Transactional
    public Ponto criaPonto(Ponto ponto) {
        var matricula = ponto.getId().getMatricula();
        usuarioAtualService.permissoesNivelUsuario(matricula);
        var dia = ponto.getId().getDia();
        log.info("Salvando Ponto - {} - {} ", matricula, dia);
        if (this.existe(matricula, dia)) {
            throw new PontoExistenteException(matricula, dia);
        }
        // Persiste ponto com descrição e índice já definidos
        var pontoSalvo = pontoRepository.save(defineDescricaoIndice(ponto));
        // Atualiza registros associados e define no ponto retornado para permitir cálculo imediato
        var registros = registroService.atualizaRegistrosSistemaDeAcesso(pontoSalvo);
        pontoSalvo.setRegistros(registros);
        return pontoSalvo;

    }

    private Ponto defineDescricaoIndice(Ponto ponto) {
        var matricula = ponto.getId().getMatricula();
        var dia = ponto.getId().getDia();
        var ausencia = ausenciaService.buscaAusenciaServidorNoDia(matricula, dia);
        var feriadoResponse = feriadoService.buscaFeriadoDoDia(dia);
        var descricao = defineDescricao(dia, ausencia, feriadoResponse);
        var indice = defineIndice(dia, ausencia, feriadoResponse);
        ponto.setDescricao(descricao);
        ponto.setIndice(indice);
        return ponto;
    }


    /**
     * Atualiza um ponto existente, recalculando descrição e índice,
     * persiste no repositório e atualiza registros associados.
     *
     * @param ponto objeto a ser atualizado
     * @return ponto atualizado
     * @throws PontoInexistenteException se não existir ponto para matrícula e dia informados
     */
    public Ponto atualizaPonto(Ponto ponto) {
        var matricula = ponto.getId().getMatricula();
        usuarioAtualService.permissoesNivelUsuario(matricula);
        var dia = ponto.getId().getDia();
        log.info("Atualizando Ponto - {} - {} ", matricula, dia);
        if (this.existe(matricula, dia)) {
            // Recalcula descrição e índice
            defineDescricaoIndice(ponto);
            // Atualiza registros e define no ponto
            var registros = registroService.atualizaRegistrosSistemaDeAcesso(ponto);
            ponto.setRegistros(registros);
            // Persiste alterações
            var pontoAtualizado = pontoRepository.save(ponto);
            return pontoAtualizado;
        }
        throw new PontoInexistenteException(matricula, dia);
    }

    /**
     * Carrega ou cria pontos em um intervalo de datas. Para cada dia do período:
     * <ul>
     *   <li>Se existir ponto, atualiza os registros;</li>
     *   <li>Se não existir, cria novo ponto;</li>
     * </ul>
     *
     * @param matricula matrícula do servidor
     * @param inicio    data inicial do período
     * @param fim       data final do período
     * @return lista de pontos carregados ou criados
     */
    public List<Ponto> carregaPontos(String matricula, LocalDate inicio, LocalDate fim) {

        usuarioAtualService.permissoesNivelUsuario(matricula);

        List<Ponto> pontos = pontoRepository.buscaPontosPorPeriodo(matricula, inicio, fim);

        pontos.forEach(ponto -> {
            var registros = registroService.atualizaRegistrosSistemaDeAcesso(ponto);
            ponto.setRegistros(registros);
        });

        LocalDate dataAtual = inicio;
        while (!dataAtual.isAfter(fim)) {
            var id = PontoId.builder().
                    dia(dataAtual).
                    matricula(matricula).
                    build();
            if (pontos.contains(Ponto.builder().id(id).build())) {
                dataAtual = dataAtual.plusDays(1);
                continue;
            }
            var ponto = Ponto.builder().id(id).build();
            ponto = criaPonto(ponto);
            pontos.add(ponto);
            dataAtual = dataAtual.plusDays(1); // Avança para o próximo dia
        }
        return pontos;
    }
}
