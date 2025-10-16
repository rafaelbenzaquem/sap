package br.jus.trf1.sipe.relatorio.model;

import br.jus.trf1.sipe.ausencia.Ausencia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRelatorioLotacaoModel {

    private String nome;
    private String matricula;
    private String lotacao;
    private String descricao;
    private Integer horasDiaria;
    private List<Ausencia> ausencias;


    public UsuarioRelatorioLotacaoModel(String nome, String matricula, String lotacao, String descricao) {
        this.nome = nome;
        this.matricula = matricula;
        this.lotacao = lotacao;
        this.descricao = descricao;
        this.horasDiaria = 0;
        this.ausencias = new ArrayList<>();
    }
}
