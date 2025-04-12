package br.jus.trf1.sipe.usuario.exceptions;

import br.jus.trf1.sipe.usuario.Usuario;

import static br.jus.trf1.sipe.comum.util.ConstantesParaDataTempo.MSG_ENTIDADE_INEXISTENTE;

public class UsuarioInexistenteException extends RuntimeException {

    public UsuarioInexistenteException(Integer id) {
        this(MSG_ENTIDADE_INEXISTENTE.formatted(Usuario.class.getSimpleName(), id));
    }

    public UsuarioInexistenteException(String msg) {
        super(msg);
    }

}
