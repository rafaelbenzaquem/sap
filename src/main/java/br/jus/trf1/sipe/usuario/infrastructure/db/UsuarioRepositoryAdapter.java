package br.jus.trf1.sipe.usuario.infrastructure.db;

import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.domain.port.out.UsuarioRepositoryPort;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioInexistenteException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository usuarioJpaRepository;

    @Override
    public List<Usuario> paginaPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula, int page, int size) {
        Page<UsuarioJpa> pageResult = usuarioJpaRepository.paginaPorNomeOuCrachaOuMatricula(nome, cracha, matricula, PageRequest.of(page, size));
        return pageResult.getContent().stream().map(UsuarioJpaMapper::toDomain).toList();
    }

    @Override
    public List<Usuario> pagina(int page, int size) {
        Page<UsuarioJpa> pageResult = usuarioJpaRepository.findAll(PageRequest.of(page, size));
        return pageResult.getContent().stream().map(UsuarioJpaMapper::toDomain).toList();
    }


    @Override
    public List<Usuario> listaPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula) {
        List<UsuarioJpa> usuarios = usuarioJpaRepository.listaPorNomeOuCrachaOuMatricula(nome, cracha, matricula);
        return usuarios.stream().map(UsuarioJpaMapper::toDomain).toList();
    }

    @Override
    public List<Usuario> lista() {
        return usuarioJpaRepository.findAll().stream().map(UsuarioJpaMapper::toDomain).toList();
    }

    @Override
    public long conta() {
        return usuarioJpaRepository.count();
    }

    @Override
    public long contaPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula) {
         return usuarioJpaRepository.countAllByNomeOrCrachaOrMatricula(nome,cracha,matricula);
    }

    @Override
    public Optional<Usuario> buscaPorId(Integer id) {
        return usuarioJpaRepository.findById(id).map(UsuarioJpaMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscaPorMatricula(String matricula) {
        return usuarioJpaRepository.findUsuarioByMatricula(matricula).map(UsuarioJpaMapper::toDomain);
    }

    @Override
    public boolean checaSeExisteUsuarioComMatricula(String matricula, Integer id) {
        return usuarioJpaRepository.checaSeExisteUsuarioComMatricula(matricula, id);
    }

    @Override
    public boolean checaSeExisteUsuarioComCracha(Integer cracha, Integer id) {
        return usuarioJpaRepository.checaSeExisteUsuarioComCracha(cracha, id);
    }

    @Override
    public Usuario salva(Usuario usuario) {
        var usuarioJpa = UsuarioJpaMapper.toEntity(usuario);
        usuarioJpa = usuarioJpaRepository.save(usuarioJpa);
        return UsuarioJpaMapper.toDomain(usuarioJpa);
    }

    @Override
    public Usuario apagarPorId(Integer id) {
        usuarioJpaRepository.deleteById(id);
        var vinculoOpt = usuarioJpaRepository.findById(id);
        if (vinculoOpt.isPresent()) {
            usuarioJpaRepository.deleteById(id);
            return UsuarioJpaMapper.toDomain(vinculoOpt.get());
        }
        throw new UsuarioInexistenteException(id);
    }
}