package br.jus.trf1.sipe.ponto.domain.service;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jsarh.AusenciaJSarhAdapter;
import br.jus.trf1.sipe.feriado.infrastructure.jsarh.FeriadoJSarhClient;
import br.jus.trf1.sipe.feriado.infrastructure.jsarh.dto.FeriadoJSarhResponse;
import br.jus.trf1.sipe.ponto.domain.model.IndicePonto;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.ponto.domain.model.PontoId;
import br.jus.trf1.sipe.ponto.domain.port.in.PontoServicePort;
import br.jus.trf1.sipe.ponto.domain.port.out.PontoPersistencePort;
import br.jus.trf1.sipe.ponto.exceptions.PontoExistenteException;
import br.jus.trf1.sipe.ponto.exceptions.PontoInexistenteException;
import br.jus.trf1.sipe.registro.domain.port.in.RegistroServicePort;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.domain.port.out.UsuarioSecurityPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static br.jus.trf1.sipe.comum.util.DataTempoUtil.paraString;

/**
 * Serviço para gerenciamento de pontos de registro de servidores.
 *
 * <p>Fornece operações de criação, atualização, busca e carregamento de pontos,
 * integrando-se com serviços externos de ausências e feriados e atualizando
 * registros conforme regras de negócio.</p>
 */
@Slf4j
@Service
public class PontoServiceAdapter implements PontoServicePort {

    private final PontoPersistencePort pontoPersistencePort;
    private final RegistroServicePort registroServicePort;
    private final AusenciaJSarhAdapter ausenciaService;
    private final FeriadoJSarhClient feriadoService;
    private final UsuarioSecurityPort usuarioSecurityPort;

    public PontoServiceAdapter(PontoPersistencePort pontoJpaRepository,
                               RegistroServicePort registroServicePort,
                               AusenciaJSarhAdapter ausenciaService,
                               FeriadoJSarhClient feriadoService,
                               UsuarioSecurityPort usuarioSecurityPort) {
        this.pontoPersistencePort = pontoJpaRepository;
        this.registroServicePort = registroServicePort;
        this.ausenciaService = ausenciaService;
        this.feriadoService = feriadoService;
        this.usuarioSecurityPort = usuarioSecurityPort;
    }



    @Override
    public Boolean existePontoComPedidoAlteracaoPendenteNoPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        return pontoPersistencePort.existePontosComAlteracaoRegistroPendentePorData(matricula, inicio, fim);
    }

    /**
     * Verifica a existência de um ponto para a matrícula e dia informados.
     *
     * @param matricula matrícula do servidor
     * @param dia       dia do ponto a verificar
     * @return {@code true} se o ponto existir, {@code false} caso contrário
     */
    @Override
    public boolean existe(String matricula, LocalDate dia) {
        return pontoPersistencePort.existe(matricula, dia);
    }

    /**
     * Busca um ponto específico por matrícula e dia.
     *
     * @param matricula matrícula do servidor
     * @param dia       data do ponto a buscar
     * @return ponto encontrado
     */
    @Override
    public Ponto buscaPonto(String matricula, LocalDate dia) {
        log.info("Buscando Ponto - {} - {} ", paraString(dia), matricula);
        usuarioSecurityPort.permissoesNivelUsuario(matricula);
        return pontoPersistencePort.busca(matricula, dia);
    }


    /**
     * Retorna lista de pontos em um intervalo de datas para a matrícula informada.
     *
     * @param matricula matrícula do servidor
     * @param inicio    data de início do período
     * @param fim       data de fim do período
     * @return lista de pontos no período
     */
    @Override
    public List<Ponto> buscarPontos(String matricula, LocalDate inicio, LocalDate fim) {
        log.info("Lista de Pontos - {} - {} - {} ", paraString(inicio), paraString(fim), matricula);
        usuarioSecurityPort.permissoesNivelUsuario(matricula);
        return pontoPersistencePort.buscaPontosPorPeriodo(matricula, inicio, fim);
    }

    /**
     * Monta a descrição do ponto incluindo dia da semana, eventual ausência e feriado.
     *
     * @param dia      data do ponto
     * @param ausencia optional com dados de ausência externa
     * @param feriado  optional com dados de feriado externo
     * @return descrição completa formatada
     */
    private String defineDescricao(LocalDate dia, Optional<Ausencia> ausencia, Optional<FeriadoJSarhResponse> feriado) {
        return dia.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.of("pt", "BR")) + "\n" +
                ausencia.map(a -> ", " + a.getDescricao()).orElse("") + "\n" +
                feriado.map(f -> ", " + f.descricao()).orElse("");
    }

    /**
     * Define o índice do ponto conforme dia, ausência ou feriado.
     *
     * @param dia      data do ponto
     * @param ausencia optional com dados de ausência externa
     * @param feriado  optional com dados de feriado externo
     * @return índice do ponto (AUSENCIA, DOMINGO_E_FERIADOS, SABADO ou DIA_UTIL)
     */
    private IndicePonto defineIndice(LocalDate dia, Optional<Ausencia> ausencia, Optional<FeriadoJSarhResponse> feriado) {
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
     * @throws PontoExistenteException se já existir pontoJpa para matrícula e dia informados
     */
    @Override
    public Ponto criaPonto(Ponto ponto) {
        var matricula = ponto.getId().getUsuario().getMatricula();
        usuarioSecurityPort.permissoesNivelUsuario(matricula);
        var dia = ponto.getId().getDia();
        log.info("Salvando Ponto - {} - {} ", matricula, dia);
        var pontoSalvo = pontoPersistencePort.salva(defineDescricaoIndice(ponto));
        return pontoSalvo;

    }

    private Ponto defineDescricaoIndice(Ponto ponto) {
        var matricula = ponto.getId().getUsuario().getMatricula();
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
    @Override
    public Ponto atualizaPonto(Ponto ponto) {
        var matricula = ponto.getId().getUsuario().getMatricula();
        usuarioSecurityPort.permissoesNivelUsuario(matricula);
        var dia = ponto.getId().getDia();
        log.info("Atualizando Ponto - {} - {} ", matricula, dia);
        if (this.existe(matricula, dia)) {
            // Recalcula descrição e índice
            defineDescricaoIndice(ponto);
            // Atualiza registros e define no ponto
            var registros = registroServicePort.atualizaRegistrosSistemaDeAcesso(ponto);
            ponto.setRegistros(registros);
            // Persiste alterações
            var pontoAtualizado = pontoPersistencePort.salva(ponto);
            return pontoAtualizado;
        }
        throw new PontoInexistenteException(matricula, dia);
    }

    /**
     * Carrega ou cria pontos num intervalo de datas. Para cada dia do período:
     * <ul>
     *   <li>Se existir ponto, atualiza os registros;</li>
     *   <li>Se não existir, cria um ponto;</li>
     * </ul>
     *
     * @param matricula matrícula do servidor
     * @param inicio    data inicial do período
     * @param fim       data final do período
     * @return lista de pontoJpas carregados ou criados
     */
    @Override
    public List<Ponto> carregaPontos(String matricula, LocalDate inicio, LocalDate fim) {

        usuarioSecurityPort.permissoesNivelUsuario(matricula);

        List<Ponto> pontosImutaveis = pontoPersistencePort.buscaPontosPorPeriodo(matricula, inicio, fim);

        List<Ponto> pontos = new ArrayList<>(pontosImutaveis);

        pontos.forEach(ponto -> {
            var registros = registroServicePort.atualizaRegistrosSistemaDeAcesso(ponto);
            ponto.setRegistros(registros);
        });

        LocalDate dataAtual = inicio;
        while (!dataAtual.isAfter(fim)) {
            var id = PontoId.builder().
                    dia(dataAtual).
                    usuario(Usuario.builder()
                            .matricula(matricula)
                            .build()).
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
