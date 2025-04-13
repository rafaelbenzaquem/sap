package br.jus.trf1.sipe.externo.jsarh.ausencias.ferias;

import lombok.Getter;

@Getter
public enum Ocorrencias {

    SUSPENSAS(7), MARCADAS(1), GOZADAS(2), INTERROMPDIAS(5);

    private final int valor;

    Ocorrencias(int valor) {
        this.valor = valor;
    }

    public static Ocorrencias valueOf(int value) {
        return switch (value) {
            case 7 -> SUSPENSAS;
            case 1 -> MARCADAS;
            case 2 -> GOZADAS;
            case 5 -> INTERROMPDIAS;
            default -> throw new IllegalArgumentException();
        };
    }

    public static Ocorrencias parse(String name) {
        String upperName = name.toUpperCase();
        return switch (upperName) {
            case "SUSPENSAS" -> SUSPENSAS;
            case "MARCADAS" -> MARCADAS;
            case "GOZADAS" -> GOZADAS;
            case "INTERROMPDIAS" -> INTERROMPDIAS;
            default -> throw new IllegalArgumentException();
        };
    }

}
