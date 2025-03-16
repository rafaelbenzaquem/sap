package br.jus.trf1.sap.registro.exceptions;

import br.jus.trf1.sap.registro.Registro;

import static br.jus.trf1.sap.comum.util.ConstantesDataTempoUtil.MSG_ENTIDADE_EXISTENTE;

public class RegistroInexistenteException extends RuntimeException {

    public RegistroInexistenteException(String message) {
        super(message);
    }

    public RegistroInexistenteException(Long id) {
        this(MSG_ENTIDADE_EXISTENTE.formatted(Registro.class.getSimpleName(), id.toString()));
    }

    public RegistroInexistenteException(Registro registro) {
        this(registro.getId());
    }
}
