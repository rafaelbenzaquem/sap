package br.jus.trf1.sap.registro.exceptions;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.registro.Registro;
import br.jus.trf1.sap.comum.util.DataTempoUtil;

import java.time.LocalDate;

import static br.jus.trf1.sap.comum.util.ConstantesParaDataTempo.PADRAO_SAIDA_DATA;

public class RegistroExistenteSalvoEmPontoDifenteException extends RuntimeException {

    public RegistroExistenteSalvoEmPontoDifenteException(Registro registro) {
        this(registro.getPonto());
    }

    public RegistroExistenteSalvoEmPontoDifenteException(Ponto ponto) {
        this(ponto.getId().getMatricula(), ponto.getId().getDia());
    }


    public RegistroExistenteSalvoEmPontoDifenteException(String matricula, LocalDate dia) {
        this("Registro foi salvo anteriormente no Ponto id:{\"matricula\":\"%s\", \"dia\":\"%s\"}".
                formatted(matricula, DataTempoUtil.paraString(dia, PADRAO_SAIDA_DATA)));
    }

    public RegistroExistenteSalvoEmPontoDifenteException(String message) {
        super(message);
    }
}
