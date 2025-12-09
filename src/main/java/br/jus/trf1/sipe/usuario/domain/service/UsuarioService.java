package br.jus.trf1.sipe.usuario.domain.service;

import br.jus.trf1.sipe.comum.exceptions.CamposUnicosExistentesException;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.domain.port.in.UsuarioServicePort;
import br.jus.trf1.sipe.usuario.domain.port.out.UsuarioAtualPort;
import br.jus.trf1.sipe.usuario.domain.port.out.UsuarioRepositoryPort;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioInexistenteException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UsuarioServicePort {

    private final UsuarioRepositoryPort usuarioRepository;
    private final UsuarioAtualPort usuarioAtualPort;

    @Override
    public Page<Usuario> buscaPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula, Pageable pageable) {
        List<Usuario> usuarios = usuarioRepository.findAllByNomeOrCrachaOrMatricula(nome, cracha, matricula, pageable.getPageNumber(), pageable.getPageSize());
        long total = usuarioRepository.countByNomeOrCrachaOrMatricula(nome, cracha, matricula);
        return new PageImpl<>(usuarios, pageable, total);
    }

    @Override
    public Usuario getUsuarioAtual() {
        return usuarioAtualPort.getUsuario();
    }

    @Override
    public Page<Usuario> listar(Pageable pageable) {
        // Esta implementação do JpaRepository não tem um findAll que aceita apenas Pageable.
        // Assumindo que listar todos é o mesmo que buscar sem filtros.
        return buscaPorNomeOuCrachaOuMatricula(null, null, null, pageable);
    }

    @Override
    public Usuario buscaPorMatricula(String matricula) {
        return usuarioRepository.findUsuarioByMatricula(matricula)
                .orElseThrow(() -> new UsuarioInexistenteException("Não existe usuário para matrícula: %s!".formatted(matricula)));
    }

    @Override
    public Usuario buscaPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioInexistenteException(id));
    }

    @Override
    public Usuario salve(Usuario usuario) {
        var mapCampoMensagem = new HashMap<String, String>();

        if (usuarioRepository.checaSeExisteUsuarioComCracha(usuario.getCracha(), usuario.getId())) {
            mapCampoMensagem.put("cracha", "Existe usuário com crachá = " + usuario.getCracha());
        }
        if (usuarioRepository.checaSeExisteUsuarioComMatricula(usuario.getMatricula(), usuario.getId())) {
            mapCampoMensagem.put("matricula", "Existe usuário com matrícula = " + usuario.getMatricula());
        }

        if (mapCampoMensagem.isEmpty()) {
            return usuarioRepository.save(usuario);
        }
        throw new CamposUnicosExistentesException(mapCampoMensagem);
    }

    @Override
    public boolean permissaoDiretor() {
        return usuarioAtualPort.ehDiretor();
    }

    @Override
    public boolean permissaoAdministrador() {
        return usuarioAtualPort.ehAdmin();
    }
}