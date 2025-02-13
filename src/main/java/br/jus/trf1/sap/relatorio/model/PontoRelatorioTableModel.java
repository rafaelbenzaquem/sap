package br.jus.trf1.sap.relatorio.model;

import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PontoRelatorioTableModel {

    private Integer numero;
    private String dia;
    private String descricao;

}
