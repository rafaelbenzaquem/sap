package br.jus.trf1.sap.relatorio.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RelatorioModel {

    private Integer numero;
    private String dia;
    private String hora;
    private String sentido;
    private String matricula;
    private String descricao;

}
