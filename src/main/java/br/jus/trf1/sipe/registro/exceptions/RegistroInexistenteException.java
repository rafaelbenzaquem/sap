package br.jus.trf1.sipe.registro.exceptions;

import br.jus.trf1.sipe.registro.Registro;

import static br.jus.trf1.sipe.comum.util.PadroesDeMensagem.MSG_ENTIDADE_INEXISTENTE;


public class RegistroInexistenteException extends RuntimeException {

    public RegistroInexistenteException(String message) {
        super(message);
    }

    public RegistroInexistenteException(Long id) {
        this(MSG_ENTIDADE_INEXISTENTE.formatted(Registro.class.getSimpleName(), id.toString()));
    }

    public RegistroInexistenteException(Registro registro) {
        this(registro.getId());
    }
}
