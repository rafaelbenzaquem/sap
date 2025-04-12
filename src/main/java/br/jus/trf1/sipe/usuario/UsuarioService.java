package br.jus.trf1.sipe.usuario;

import br.jus.trf1.sipe.usuario.exceptions.UsuarioInexistenteException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public Page<Usuario> buscarVinculosPorNomeOuCrachaOuMatricula(String nome,
                                                                  String cracha,
                                                                  String matricula,
                                                                  Pageable pageable) {
        return usuarioRepository.findAllByNomeOrCrachaOrMatricula(nome, cracha, matricula, pageable);
    }

    public Page<Usuario> listar(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscaPorMatricula(String matricula) {
        return usuarioRepository.findUsuarioByMatricula(matricula).
                orElseThrow(() -> new UsuarioInexistenteException("Não existe usuário para matrícula: %s!"
                        .formatted(matricula)));
    }

    public Usuario buscaPorId(Integer id) {
        return usuarioRepository.findById(id).
                orElseThrow(() -> new UsuarioInexistenteException(id));
    }

}
