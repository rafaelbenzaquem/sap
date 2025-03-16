package br.jus.trf1.sap.ponto.exceptions;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoId;

import java.time.LocalDate;

import static br.jus.trf1.sap.comum.util.ConstantesDataTempoUtil.MSG_ENTIDADE_EXISTENTE;

public class PontoInexistenteException extends RuntimeException {


    public PontoInexistenteException(String matricula, LocalDate dia) {
        this(MSG_ENTIDADE_EXISTENTE.formatted(Ponto.class.getSimpleName(), PontoId.builder().
                matricula(matricula).
                dia(dia).
                build().
                toString()));
    }

    public PontoInexistenteException(String message) {
        super(message);
    }
}
