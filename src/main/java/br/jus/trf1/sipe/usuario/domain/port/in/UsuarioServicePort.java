package br.jus.trf1.sipe.usuario.domain.port.in;

import br.jus.trf1.sipe.usuario.domain.model.Usuario;

import java.util.List;

public interface UsuarioServicePort {

    List<Usuario> buscaPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula, int page, int size);

    Usuario getUsuarioAutenticado();

    List<Usuario> listar(int page, int size);

    Usuario buscaPorMatricula(String matricula);

    Usuario buscaPorId(Integer id);

    Usuario salve(Usuario usuario);

    Usuario apagaPorId(Integer id);

    boolean permissaoDiretor();

    void temPermissaoRecurso(Object recurso);

    boolean permissaoAdministrador();
}