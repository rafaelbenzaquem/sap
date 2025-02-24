package br.jus.trf1.sap.registro;

import lombok.Getter;

@Getter
public enum Sentido {

    ENTRADA('E', "Entrada"), SAIDA('S', "Saída");

    private final Character codigo;
    private final String palavra;

    Sentido(Character codigo, String palavra) {
        this.codigo = codigo;
        this.palavra = palavra;
    }

    public static Sentido toEnum(Character codigo) {
        if (codigo == null)
            return null;

        for (Sentido tc : Sentido.values())
            if (codigo.equals(tc.codigo))
                return tc;

        throw new IllegalArgumentException("Código inválido:" + codigo);
    }

    public static Sentido toEnum(String palavra) {
        if (palavra == null)
            return null;

        for (Sentido sentido : Sentido.values())
            if (palavra.equalsIgnoreCase(sentido.palavra))
                return sentido;

        throw new IllegalArgumentException("Palavra inválida:" + palavra);
    }
}
