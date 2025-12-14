package br.jus.trf1.sipe.registro.exceptions;

import br.jus.trf1.sipe.ponto.infrastructure.jpa.PontoJpa;
import br.jus.trf1.sipe.registro.Registro;
import br.jus.trf1.sipe.comum.util.DataTempoUtil;

import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_SAIDA_DATA;

public class RegistroExistenteException extends RuntimeException {

    public RegistroExistenteException(Registro registro) {
        this(registro.getPonto());
    }

    public RegistroExistenteException(PontoJpa pontoJpa) {
        this(pontoJpa.getId().getUsuario().getMatricula(), pontoJpa.getId().getDia());
    }


    public RegistroExistenteException(String matricula, LocalDate dia) {
        this("Registro foi salvo anteriormente no Ponto id:{\"matricula\":\"%s\", \"dia\":\"%s\"}".
                formatted(matricula, DataTempoUtil.paraString(dia, PADRAO_SAIDA_DATA)));
    }

    public RegistroExistenteException(String message) {
        super(message);
    }
}
