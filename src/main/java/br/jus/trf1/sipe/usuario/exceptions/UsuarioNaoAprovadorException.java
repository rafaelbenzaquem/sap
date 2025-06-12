package br.jus.trf1.sipe.usuario.exceptions;

public class UsuarioNaoAprovadorException extends RuntimeException {
    public UsuarioNaoAprovadorException(Integer idUsuario) {
        super("Usuário com o ID: " + idUsuario+" não pode aprovar registro!");
    }
}
