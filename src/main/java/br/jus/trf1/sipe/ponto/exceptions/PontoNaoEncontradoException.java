package br.jus.trf1.sipe.ponto.exceptions;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.PontoId;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.ConstantesParaDataTempo.MSG_ENTIDADE_INEXISTENTE;

@Slf4j
public class PontoNaoEncontradoException extends RuntimeException {


    public PontoNaoEncontradoException(String matricula, LocalDate dia) {
        this(MSG_ENTIDADE_INEXISTENTE.formatted(Ponto.class.getSimpleName(), PontoId.builder().
                matricula(matricula).
                dia(dia).
                build().
                toString()));
    }

    public PontoNaoEncontradoException(String message) {
        super(message);
        log.warn(message);
    }
}
