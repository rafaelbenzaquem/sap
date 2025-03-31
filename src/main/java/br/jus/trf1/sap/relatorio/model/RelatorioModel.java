package br.jus.trf1.sap.relatorio.model;

import br.jus.trf1.sap.externo.jsarh.ausencias.Ausencia;
import br.jus.trf1.sap.externo.jsarh.feriado.Feriado;
import br.jus.trf1.sap.ponto.Ponto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static br.jus.trf1.sap.relatorio.model.util.CalculadoraPeriodosUtil.*;
import static br.jus.trf1.sap.relatorio.model.util.FomatadorTextoUtil.*;

/**
 * Representa o modelo de um relatório, contendo os dados necessários para geração.
 */
@Slf4j
@Getter
public class RelatorioModel {


    private final UsuarioModel usuario;
    private final List<Ponto> pontos;
    private final Long diasUteis;
    private final Duration permanenciaTotal;
    private final Duration horasCreditoOuDebito;
    private final List<PontoModel> pontoModels;
    private final JRBeanCollectionDataSource pontosDataSource;


    /**
     * Constrói o modelo de relatório com base no usuário e nos pontos registrados.
     *
     * @param usuario Modelo do usuário.
     * @param pontos  Lista de pontos registrados.
     */
    public RelatorioModel(UsuarioModel usuario, List<Ponto> pontos, List<Feriado> feriados, List<Ausencia> ausencias) {
        log.debug("Contruindo RelatorioModel...");
        this.usuario = Objects.requireNonNull(usuario, "Usuário não pode ser nulo");
        this.pontos = Objects.requireNonNull(pontos, "Lista de pontos não pode ser nula");
        var datas = gerarDiasAusentes(ausencias);
         feriados.forEach(feriado -> datas.add(feriado.getData()));
        this.diasUteis = calculaDiasUteis(pontos, datas);
        this.permanenciaTotal = calculaPermanenciaTotal(pontos);
        this.horasCreditoOuDebito = calculaHorasDebitoOuCredito(permanenciaTotal, diasUteis, usuario.horasDiaria());
        this.pontoModels = carregarDadosPontos(pontos, feriados, ausencias);
        this.pontosDataSource = new JRBeanCollectionDataSource(pontoModels, false);
    }


    public String getTextoHorasUteis() {
        return formataTextoHorasUteis(diasUteis, usuario.horasDiaria());
    }

    public String getTextoPermanenciaTotal() {
        return formataTextoDuracao(permanenciaTotal);
    }

    public String getRotuloHorasCreditoOuDebito() {
        return formataRotuloHorasCreditoOuDebito(horasCreditoOuDebito);
    }

    public String getTextoHorasCreditoOuDebito() {
        return formataTextoDuracao(horasCreditoOuDebito);
    }

    private List<PontoModel> carregarDadosPontos(List<Ponto> pontos, List<Feriado> feriados, List<Ausencia> ausencias) {
        return pontos.stream()
                .map(ponto -> {

                    // Verifica se a data do ponto está dentro de um período de ausência
                    Ausencia ausenciaCorrespondente = ausencias.stream()
                            .filter(ausencia -> !ponto.getId().getDia().isBefore(ausencia.getInicio()) &&
                                    !ponto.getId().getDia().isAfter(ausencia.getFim()))
                            .findFirst()
                            .orElse(null);
                    // Verifica se a data do ponto é um feriado
                    Feriado feriadoCorrespondente = feriados.stream()
                            .filter(feriado -> feriado.getData().equals(ponto.getId().getDia()))
                            .findFirst()
                            .orElse(null);

                    var descricao = feriadoCorrespondente == null ? null : feriadoCorrespondente.getDescricao();
                    descricao = ausenciaCorrespondente == null ? descricao : ausenciaCorrespondente.getDescricao();

                    return descricao == null ? new PontoModel(ponto) :
                            new PontoModel(ponto, descricao);
                }).toList();
    }
}
