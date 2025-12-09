package br.jus.trf1.sipe.usuario.domain.port.out;

import br.jus.trf1.sipe.usuario.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {

    List<Usuario> findAllByNomeOrCrachaOrMatricula(String nome, Integer cracha, String matricula, int page, int size);

    long countByNomeOrCrachaOrMatricula(String nome, Integer cracha, String matricula);

    Optional<Usuario> findById(Integer id);

    Optional<Usuario> findUsuarioByMatricula(String matricula);

    boolean checaSeExisteUsuarioComMatricula(String matricula, Integer id);

    boolean checaSeExisteUsuarioComCracha(Integer cracha, Integer id);

    Usuario save(Usuario usuario);

    void deleteById(Integer id);
}