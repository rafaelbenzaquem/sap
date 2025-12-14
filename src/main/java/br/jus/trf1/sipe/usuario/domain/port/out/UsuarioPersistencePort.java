package br.jus.trf1.sipe.usuario.domain.port.out;

import br.jus.trf1.sipe.usuario.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioPersistencePort {

    List<Usuario> paginaPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula, int page, int size);

    List<Usuario> pagina(int page, int size);

    List<Usuario> listaPorNomeOuCrachaOuMatricula(String nome,Integer cracha,String matricula);

    List<Usuario> lista();

    long conta();

    long contaPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula);

    Optional<Usuario> buscaPorId(Integer id);

    Optional<Usuario> buscaPorMatricula(String matricula);

    boolean checaSeExisteUsuarioComMatricula(String matricula, Integer id);

    boolean checaSeExisteUsuarioComCracha(Integer cracha, Integer id);

    Usuario salva(Usuario usuario);

    Usuario apagarPorId(Integer id);
}