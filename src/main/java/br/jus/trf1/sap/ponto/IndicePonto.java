package br.jus.trf1.sap.ponto;

import lombok.Getter;

@Getter
public enum IndicePonto {
    AUSENCIA(0f), DIA_UTIL(1.0f), SABADO(1.5f), DOMINGO_E_FERIADOS(2.0f);

    final float valor;

    IndicePonto(float valor) {
        this.valor = valor;
    }

    public static IndicePonto toEnum(Float valor) {
        if (valor == null)
            return null;

        for (IndicePonto ip : IndicePonto.values())
            if (valor.equals(ip.valor))
                return ip;

        throw new IllegalArgumentException("Índice inválido:" + valor);
    }
}
