package br.jus.trf1.sipe.relatorio.model;

import br.jus.trf1.sipe.ausencia.Ausencia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioModel {

    private String nome;
    private String matricula;
    private String cargo;
    private String funcao;
    private String lotacao;
    private String descricao;
    private Integer horasDiaria;
    private List<Ausencia> ausencias;


    /**
     * Representa o modelo de um usuário para geração de relatórios.
     *
     * @param nome        Nome do usuário.
     * @param cargo       Cargo do usuário.
     * @param funcao      Função do usuário.
     * @param matricula   Matrícula do usuário.
     * @param lotacao     Lotação do usuário.
     * @param horasDiaria Quantidade de horas diárias trabalhadas.
     * @param ausencias   Lista de ausências encontradas para o usuário
     */
    public UsuarioModel(String nome, String matricula, String cargo, String funcao, String lotacao, Integer horasDiaria, String descricao, List<Ausencia> ausencias) {
        this.nome = nome;
        this.matricula = matricula;
        this.cargo = cargo;
        this.funcao = funcao;
        this.lotacao = lotacao;
        this.descricao = descricao;
        this.horasDiaria = horasDiaria;
        this.ausencias = ausencias;
    }
}
