package br.jus.trf1.sipe.usuario;

import br.jus.trf1.sipe.comum.exceptions.CamposUnicosExistentesException;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioInexistenteException;
import br.jus.trf1.sipe.usuario.infrastructure.persistence.UsuarioJpa;
import br.jus.trf1.sipe.usuario.infrastructure.persistence.UsuarioJpaRepository;
import br.jus.trf1.sipe.usuario.infrastructure.security.UsuarioSecurityAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class UsuarioService {

    private final UsuarioJpaRepository usuarioRepository;
    private final UsuarioSecurityAdapter usuarioSecurityAdapter;

    public UsuarioService(UsuarioJpaRepository usuarioRepository, UsuarioSecurityAdapter usuarioSecurityAdapter) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioSecurityAdapter = usuarioSecurityAdapter;
    }


    public Page<UsuarioJpa> buscaPorNomeOuCrachaOuMatricula(String nome,
                                                            Integer cracha,
                                                            String matricula,
                                                            Pageable pageable) {
        return usuarioRepository.findAllByNomeOrCrachaOrMatricula(nome, cracha, matricula, pageable);
    }

    public Usuario getUsuarioAtual() {
        return usuarioSecurityAdapter.getUsuario();
    }

    public void permissaoRecurso(Ponto ponto) {
        Objects.requireNonNull(ponto);
        Objects.requireNonNull(ponto.getId());
        Objects.requireNonNull(ponto.getId().getUsuarioJPA().getMatricula());
        usuarioSecurityAdapter.permissoesNivelUsuario(ponto.getId().getUsuarioJPA().getMatricula());
    }

    public Page<UsuarioJpa> listar(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }


    public UsuarioJpa buscaPorMatricula(String matricula) {
        return usuarioRepository.findUsuarioByMatricula(matricula)
                .orElseThrow(() -> new UsuarioInexistenteException("Não existe usuário para matrícula: %s!"
                        .formatted(matricula)));
    }

    public UsuarioJpa buscaPorId(Integer id) {
        return usuarioRepository.findById(id).
                orElseThrow(() -> new UsuarioInexistenteException(id));
    }


    public UsuarioJpa salve(UsuarioJpa usuarioJPA) {
        var mapCampoMensagem = new HashMap<String, String>();

        var existeCracha = usuarioRepository.checaSeExisteUsuarioComCracha(usuarioJPA.getCracha(), usuarioJPA.getId());
        var existeMatricula = usuarioRepository.checaSeExisteUsuarioComMatricula(usuarioJPA.getMatricula(), usuarioJPA.getId());

        if (existeCracha) {
            mapCampoMensagem.put("cracha", "Existe usuário com crachá = " + usuarioJPA.getCracha());
        }
        if (existeMatricula) {
            mapCampoMensagem.put("matricula", "Existe usuário com matrícula = " + usuarioJPA.getMatricula());
        }

        if (mapCampoMensagem.isEmpty()) {
            return usuarioRepository.save(usuarioJPA);
        }
        throw new CamposUnicosExistentesException(mapCampoMensagem);
    }

    public boolean permissaoDiretor() {
        return usuarioSecurityAdapter.ehDiretor();
    }

    public boolean permissaoAdministrador() {
        return usuarioSecurityAdapter.ehAdmin();
    }

}
