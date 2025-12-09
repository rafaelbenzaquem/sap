package br.jus.trf1.sipe.usuario.domain.port.out;

import br.jus.trf1.sipe.usuario.domain.model.Usuario;

public interface UsuarioSecurityPort {
    Usuario getUsuarioAutenticado();
    void permissoesNivelUsuario(String matricula);
    boolean ehDiretor();
    boolean ehAdmin();
}