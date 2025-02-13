package br.jus.trf1.sap.relatorio.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegistroRelatorioListModel {

    private String sentido;
    private String hora;

}
