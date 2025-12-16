package br.jus.trf1.sipe.ausencia.ferias.infrastructure.jsarh;

import lombok.Getter;

@Getter
public enum OcorrenciaJSarh {

    SUSPENSAS(7), MARCADAS(1), GOZADAS(2), INTERROMPDIAS(5);

    private final int valor;

    OcorrenciaJSarh(int valor) {
        this.valor = valor;
    }

    public static OcorrenciaJSarh valueOf(int value) {
        return switch (value) {
            case 7 -> SUSPENSAS;
            case 1 -> MARCADAS;
            case 2 -> GOZADAS;
            case 5 -> INTERROMPDIAS;
            default -> throw new IllegalArgumentException();
        };
    }

    public static OcorrenciaJSarh parse(String name) {
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
