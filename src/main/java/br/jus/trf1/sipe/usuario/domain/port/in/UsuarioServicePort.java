package br.jus.trf1.sipe.usuario.domain.port.in;

import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioServicePort {

    Page<Usuario> buscaPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula, Pageable pageable);

    Usuario getUsuarioAtual();

    Page<Usuario> listar(Pageable pageable);

    Usuario buscaPorMatricula(String matricula);

    Usuario buscaPorId(Integer id);

    Usuario salve(Usuario usuario);

    boolean permissaoDiretor();

    boolean permissaoAdministrador();
}