package br.jus.trf1.sipe.relatorio.model;

import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import lombok.Getter;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static br.jus.trf1.sipe.comum.util.FomatadorTextoUtil.formataTextoDia;
import static br.jus.trf1.sipe.comum.util.FomatadorTextoUtil.formataTextoTempoDiario;

/**
 * Representa um ponto no relatório, contendo os registros de entrada e saída.
 */
@Getter
public class PontoModel {

    private final LocalDate dia;
    private final String descricao;
    private final Duration permanencia;
    private final List<RegistroModel> registrosModel;
    private final JRBeanCollectionDataSource registrosDataSource;

    /**
     * Constrói o modelo de ponto com base em um registro de ponto.
     *
     * @param ponto Registro de ponto.
     */
    public PontoModel(Ponto ponto) {
        this.dia = ponto.getId().getDia();
        this.descricao = ponto.getDescricao();
        this.permanencia = ponto.getHorasPermanencia();
        this.registrosModel = populaRegistrosModel(ponto);
        this.registrosDataSource = new JRBeanCollectionDataSource(registrosModel, false);
    }

    public PontoModel(Ponto ponto, String descricao) {
        this.dia = ponto.getId().getDia();
        this.descricao = descricao;
        this.permanencia = ponto.getHorasPermanencia();
        this.registrosModel = populaRegistrosModel(ponto);
        this.registrosDataSource = new JRBeanCollectionDataSource(registrosModel, false);
    }


    public String getTextoDia() {
        return formataTextoDia(dia);
    }

    public String getTextoPermanencia() {
        return formataTextoTempoDiario(permanencia);
    }

    private List<RegistroModel> populaRegistrosModel(Ponto ponto) {
        var registrosModel = new ArrayList<>(IntStream.range(0, 12)
                .mapToObj(index -> {
                    var registros = ponto.getRegistros().stream().filter(r -> r.getRegistroNovo() == null && r.getAtivo()).toList();
                    if (index < registros.size()) {
                        var registro = registros.get(index);
                        return RegistroModel.of(registro.getSentido().getPalavra(), registro.getHora());
                    }
                    return RegistroModel.VAZIO;
                }).toList());
        Collections.sort(registrosModel);
        return registrosModel;
    }

}
