package br.jus.trf1.sipe.folha;

import lombok.Getter;

@Getter
public enum Mes {
    JANEIRO(1, "Janeiro"),
    FEVEREIRO(2, "Fevereiro"),
    MARCO(3, "Março"),
    ABRIL(4, "Abril"),
    MAIO(5, "Maio"),
    JUNHO(6, "Junho"),
    JULHO(7, "Julho"),
    AGOSTO(8, "Agosto"),
    SETEMBRO(9, "Setembro"),
    OUTUBRO(10, "Outubro"),
    NOVEMBRO(11, "Novembro"),
    DEZEMBRO(12, "Dezembro");
    private final Integer valor;
    private final String nome;

    Mes(Integer valor, String nome) {
        this.valor = valor;
        this.nome = nome;
    }

    public static Mes getMes(Integer valor) {
        for (Mes m : Mes.values()) {
            if (m.valor.equals(valor)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Valor invalido");
    }
}
