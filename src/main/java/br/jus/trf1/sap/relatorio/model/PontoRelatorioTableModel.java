package br.jus.trf1.sap.relatorio.model;

import lombok.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.ArrayList;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PontoRelatorioTableModel {

    private String dia;
    private String descricao;

    private List<RegistroRelatorioListModel> registros = new ArrayList<>();
    private JRBeanCollectionDataSource registrosDataSource;


    public JRBeanCollectionDataSource getRegistrosDataSource() {
        return new JRBeanCollectionDataSource(registros, false);
    }
}
