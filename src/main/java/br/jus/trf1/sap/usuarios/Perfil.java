package br.jus.trf1.sap.usuarios;

import lombok.Getter;

@Getter
public enum Perfil {
    GESTOR(0),USUARIO(1),ADMINISTRADOR(2);
    private final int value;

    private Perfil(int value) {
        this.value = value;
    }

    public static Perfil valueOf(int value) {
        return switch (value) {
            case 0 -> GESTOR;
            case 1 -> USUARIO;
            case 2 -> ADMINISTRADOR;
            default -> throw new IllegalArgumentException();
        };
    }

    public static Perfil parse(String name) {
        String upperName = name.toUpperCase();
        return switch (upperName) {
            case "GESTOR" -> GESTOR;
            case "USUARIO" -> USUARIO;
            case "ADMINISTRADOR" -> ADMINISTRADOR;
            default -> throw new IllegalArgumentException();
        };
    }
}
