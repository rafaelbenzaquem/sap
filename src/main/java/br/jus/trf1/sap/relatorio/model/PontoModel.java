package br.jus.trf1.sap.relatorio.model;

import br.jus.trf1.sap.ponto.Ponto;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import static br.jus.trf1.sap.util.DateTimeUtils.dataParaString;

@Slf4j
@Getter
public class PontoModel {

    private static final String PADRAO_DATA = "dd/MM/yyyy";

    private final LocalDate dia;
    private final String textoDia;
    private final String descricao;

    private final String textoPermanencia;
    private final PermanenciaModel permanenciaModel;

    private final List<RegistroModel> registrosModel = new ArrayList<>(Collections.nCopies(12, RegistroModel.VAZIO));
    private final JRBeanCollectionDataSource registrosDataSource;

    public PontoModel(Ponto ponto) {
        this.dia = ponto.getId().getDia();
        this.textoDia = formataTextoDia();
        this.descricao = ponto.getDescricao();
        this.permanenciaModel = PermanenciaModel.of(ponto.calculaHorasRegistros());
        this.textoPermanencia = permanenciaModel.getTextoPermanencia();
        populaRegistrosModel(ponto);
        this.registrosDataSource = new JRBeanCollectionDataSource(registrosModel, false);
    }

    private void populaRegistrosModel(Ponto ponto) {
        var registros = ponto.getRegistros();
        IntStream.range(0, registros.size()).forEach(
                index -> {
                    var r = registros.get(index);
                    registrosModel.set(index,
                            RegistroModel.of(r.getSentido().getPalavra(), r.getHora())
                    );
                }
        );
    }


    private String formataTextoDia() {
        return dataParaString(dia, PADRAO_DATA) + " - " + dia.getDayOfWeek().
                getDisplayName(TextStyle.SHORT, Locale.of("pt", "BR"));
    }
}
