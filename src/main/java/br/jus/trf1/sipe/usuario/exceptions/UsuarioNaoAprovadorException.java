package br.jus.trf1.sipe.usuario.exceptions;

public class UsuarioNaoAprovadorException extends RuntimeException {
    public UsuarioNaoAprovadorException(String matricula) {
        super("Usuário com o matrícula: " + matricula + " não pode aprovar registro!");
    }
}
