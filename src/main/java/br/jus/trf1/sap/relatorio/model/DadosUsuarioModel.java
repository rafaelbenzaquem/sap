package br.jus.trf1.sap.relatorio.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosUsuarioModel {

    private String nome;
    private String cargo;
    private String funcao;
    private Integer matricula;
    private String lotacao;
}
