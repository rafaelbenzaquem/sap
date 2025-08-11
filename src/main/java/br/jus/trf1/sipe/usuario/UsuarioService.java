package br.jus.trf1.sipe.usuario;

import br.jus.trf1.sipe.comum.exceptions.CamposUnicosExistentesException;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioInexistenteException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioAtualService usuarioAtualService;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioAtualService usuarioAtualService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioAtualService = usuarioAtualService;
    }


    public Page<Usuario> buscaPorNomeOuCrachaOuMatricula(String nome,
                                                         Integer cracha,
                                                         String matricula,
                                                         Pageable pageable) {
        return usuarioRepository.findAllByNomeOrCrachaOrMatricula(nome, cracha, matricula, pageable);
    }

    public Usuario getUsuarioAtual() {
        return usuarioAtualService.getUsuario();
    }

    public void permissaoRecurso(Ponto ponto) {
        Objects.requireNonNull(ponto);
        Objects.requireNonNull(ponto.getId());
        Objects.requireNonNull(ponto.getId().getMatricula());
        usuarioAtualService.permissoesNivelUsuario(ponto.getId().getMatricula());
    }

    public Page<Usuario> listar(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
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


    public Usuario salve(Usuario usuario) {
        var mapCampoMensagem = new HashMap<String, String>();

        var existeCracha = usuarioRepository.checaSeExisteUsuarioComCracha(usuario.getCracha(), usuario.getId());
        var existeMatricula = usuarioRepository.checaSeExisteUsuarioComMatricula(usuario.getMatricula(), usuario.getId());

        if (existeCracha) {
            mapCampoMensagem.put("cracha", "Existe usuário com crachá = " + usuario.getCracha());
        }
        if (existeMatricula) {
            mapCampoMensagem.put("matricula", "Existe usuário com matrícula = " + usuario.getMatricula());
        }

        if (mapCampoMensagem.isEmpty()) {
            return usuarioRepository.save(usuario);
        }
        throw new CamposUnicosExistentesException(mapCampoMensagem);
    }
}
