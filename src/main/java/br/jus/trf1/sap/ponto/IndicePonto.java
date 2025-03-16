package br.jus.trf1.sap.ponto;

import lombok.Getter;

@Getter
public enum IndicePonto {
    AUSENCIA(0f), DIA_UTIL(1.0f), SABADO(1.5f), DOMINGO_E_FERIADOS(2.0f);

    final float indice;

    IndicePonto(float indice) {
        this.indice = indice;
    }

    public static IndicePonto toEnum(Float indice) {
        if (indice == null)
            return null;

        for (IndicePonto ip : IndicePonto.values())
            if (indice.equals(ip.indice))
                return ip;

        throw new IllegalArgumentException("Índice inválido:" + indice);
    }
}
