package br.jus.trf1.sap.relatorio.model;

import lombok.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioModel {

    private DadosUsuarioModel dadosUsuario;
    private LocalDate inicio;
    private LocalDate fim;

    private List<PontoRelatorioTableModel> pontos;
    private JRBeanCollectionDataSource pontosDataSource;

    public JRBeanCollectionDataSource getPontosDataSource() {
        return new JRBeanCollectionDataSource(pontos, false);
    }
}
