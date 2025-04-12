package br.jus.trf1.sipe.relatorio.model;

import lombok.*;

import java.util.Objects;

/**
 * Representa o modelo de um usuário para geração de relatórios.
 *
 * @param nome        Nome do usuário.
 * @param cargo       Cargo do usuário.
 * @param funcao      Função do usuário.
 * @param matricula   Matrícula do usuário.
 * @param lotacao     Lotação do usuário.
 * @param horasDiaria Quantidade de horas diárias trabalhadas.
 */
@Builder
public record UsuarioModel(String nome, String cargo, String funcao, String matricula, String lotacao, Integer horasDiaria) {
    public UsuarioModel {
        Objects.requireNonNull(nome, "Nome não pode ser nulo");
        Objects.requireNonNull(cargo, "Cargo não pode ser nulo");
        Objects.requireNonNull(funcao, "Função não pode ser nulo");
        Objects.requireNonNull(matricula, "Matrícula não pode ser nula");
        Objects.requireNonNull(lotacao, "Lotação não pode ser nula");
        if (horasDiaria <= 0) {
            throw new IllegalArgumentException("Horas diárias devem ser maiores que zero");
        }
    }
}
