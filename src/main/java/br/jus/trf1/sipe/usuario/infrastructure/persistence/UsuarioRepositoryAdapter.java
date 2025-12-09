package br.jus.trf1.sipe.usuario.infrastructure.persistence;

import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.domain.port.out.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository repository;

    @Override
    public List<Usuario> findAllByNomeOrCrachaOrMatricula(String nome, Integer cracha, String matricula, int page, int size) {
        Page<Usuario> pageResult = repository.findAllByNomeOrCrachaOrMatricula(nome, cracha, matricula, PageRequest.of(page, size));
        return pageResult.getContent();
    }

    @Override
    public long countByNomeOrCrachaOrMatricula(String nome, Integer cracha, String matricula) {
        // O Page retornado pelo JpaRepository já contém o total de elementos.
        // Para evitar uma segunda query, poderíamos refatorar para retornar um objeto customizado.
        // Por simplicidade, vamos usar o count da query aqui.
        Page<Usuario> pageResult = repository.findAllByNomeOrCrachaOrMatricula(nome, cracha, matricula, PageRequest.of(0, 1));
        return pageResult.getTotalElements();
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Usuario> findUsuarioByMatricula(String matricula) {
        return repository.findUsuarioByMatricula(matricula);
    }

    @Override
    public boolean checaSeExisteUsuarioComMatricula(String matricula, Integer id) {
        return repository.checaSeExisteUsuarioComMatricula(matricula, id);
    }

    @Override
    public boolean checaSeExisteUsuarioComCracha(Integer cracha, Integer id) {
        return repository.checaSeExisteUsuarioComCracha(cracha, id);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return repository.save(usuario);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}