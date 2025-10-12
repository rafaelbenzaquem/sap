package br.jus.trf1.sipe.relatorio.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioRelatorioLotacaoModel{

    private String nome_1;
    private String matricula_1;
    private String descricao;


    public UsuarioRelatorioLotacaoModel() {
    }

    public UsuarioRelatorioLotacaoModel(String nome_1, String matricula_1, String descricao) {
        this.nome_1 = nome_1;
        this.matricula_1 = matricula_1;
        this.descricao = descricao;
    }
}
