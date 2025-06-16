package br.jus.trf1.sipe.folha;

import lombok.Getter;

@Getter
public enum Mes {
    JANEIRO(1),
    FEVEREIRO(2),
    MARCO(3),
    ABRIL(4),
    MAIO(5),
    JUNHO(6),
    JULHO(7),
    AGOSTO(8),
    SETEMBRO(9),
    OUTUBRO(10),
    NOVEMBRO(11),
    DEZEMBRO(12);
    private final Integer valor;

    Mes(Integer valor) {
        this.valor = valor;
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
