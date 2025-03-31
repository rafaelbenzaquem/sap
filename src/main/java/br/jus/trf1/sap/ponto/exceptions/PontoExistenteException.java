package br.jus.trf1.sap.ponto.exceptions;

import br.jus.trf1.sap.ponto.Ponto;

import static br.jus.trf1.sap.comum.util.ConstantesDataTempoUtil.MSG_ENTIDADE_EXISTENTE;

public class PontoExistenteException extends RuntimeException {

    public PontoExistenteException(Ponto ponto) {
        this(MSG_ENTIDADE_EXISTENTE.formatted(ponto.getClass().getSimpleName(), ponto));
    }

    public PontoExistenteException(String message) {
        super(message);
    }
}
