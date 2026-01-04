package br.jus.trf1.sipe.usuario.domain.port.in;

import br.jus.trf1.sipe.usuario.domain.model.Usuario;

import java.util.List;

public interface UsuarioServicePort {

    List<Usuario> paginaPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula, int page, int size);

    List<Usuario> pagina(int page, int size);

    List<Usuario> listaPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula);

    List<Usuario> lista();

    long contaTodos();

    long contaPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula);

    Usuario getUsuarioAutenticado();

    Usuario buscaPorMatricula(String matricula);

    Usuario buscaPorId(Integer id);

    Usuario salva(Usuario usuario);

    Usuario apagaPorId(Integer id);

    boolean permissaoDiretor();

    void temPermissaoRecurso(Object recurso);

    boolean permissaoAdministrador();
}