package br.jus.trf1.sipe.servidor.exceptions;

import br.jus.trf1.sipe.servidor.infrastructure.persistence.ServidorJpa;

import static br.jus.trf1.sipe.comum.util.PadroesDeMensagem.MSG_ENTIDADE_INEXISTENTE;


public class ServidorInexistenteException extends RuntimeException {

    public ServidorInexistenteException(Integer id) {
        this(MSG_ENTIDADE_INEXISTENTE.formatted(ServidorJpa.class.getSimpleName(), id));
    }

    public ServidorInexistenteException(String msg) {
        super(msg);
    }

}
