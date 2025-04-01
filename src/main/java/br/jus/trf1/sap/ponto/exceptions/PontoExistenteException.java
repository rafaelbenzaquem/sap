package br.jus.trf1.sap.ponto.exceptions;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoId;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

import static br.jus.trf1.sap.comum.util.ConstantesDataTempoUtil.MSG_ENTIDADE_EXISTENTE;

@Slf4j
public class PontoExistenteException extends RuntimeException {

    public PontoExistenteException(String matricula, LocalDate dia) {
        this(MSG_ENTIDADE_EXISTENTE.formatted(Ponto.class.getSimpleName(), PontoId.builder().
                matricula(matricula).
                dia(dia).
                build().
                toString()));
    }

    public PontoExistenteException(String message) {
        super(message);
        log.warn(message);
    }
}
