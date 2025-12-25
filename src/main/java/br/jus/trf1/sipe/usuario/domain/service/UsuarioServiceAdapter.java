package br.jus.trf1.sipe.usuario.domain.service;

import br.jus.trf1.sipe.comum.exceptions.CamposUnicosExistentesException;
import br.jus.trf1.sipe.comum.exceptions.RecursoInvalidoException;
import br.jus.trf1.sipe.ponto.infrastructure.jpa.PontoJpa;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.domain.port.in.UsuarioServicePort;
import br.jus.trf1.sipe.usuario.domain.port.out.UsuarioPersistencePort;
import br.jus.trf1.sipe.usuario.domain.port.out.UsuarioSecurityPort;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioInexistenteException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class UsuarioServiceAdapter implements UsuarioServicePort {

    private final UsuarioPersistencePort usuarioPersistencePort;
    private final UsuarioSecurityPort usuarioSecurityPort;

    public UsuarioServiceAdapter(UsuarioPersistencePort usuarioPersistencePort, UsuarioSecurityPort usuarioSecurityPort) {
        this.usuarioPersistencePort = usuarioPersistencePort;
        this.usuarioSecurityPort = usuarioSecurityPort;
    }

    @Override
    public Usuario getUsuarioAutenticado() {
        return usuarioSecurityPort.getUsuarioAutenticado();
    }

    @Override
    public void temPermissaoRecurso(Object recurso) {
        if(recurso instanceof PontoJpa pontoJpa) {
            Objects.requireNonNull(pontoJpa);
            Objects.requireNonNull(pontoJpa.getId());
            Objects.requireNonNull(pontoJpa.getId().getUsuario().getMatricula());
            usuarioSecurityPort.permissoesNivelUsuario(pontoJpa.getId().getUsuario().getMatricula());
        }
        throw new RecursoInvalidoException("O objeto %s não é um recurso válido!".formatted(recurso));
    }

    @Override
    public List<Usuario> paginaPorNomeOuCrachaOuMatricula(String nome,
                                                          Integer cracha,
                                                          String matricula,
                                                          int page, int size) {
        return usuarioPersistencePort.paginaPorNomeOuCrachaOuMatricula(nome, cracha, matricula, page, size);
    }
    @Override
    public List<Usuario> listaPorNomeOuCrachaOuMatricula(String nome,
                                                          Integer cracha,
                                                          String matricula) {
        return usuarioPersistencePort.listaPorNomeOuCrachaOuMatricula(nome, cracha, matricula);
    }
    @Override
    public List<Usuario> pagina(int page, int size) {
        return usuarioPersistencePort.pagina(page, size);
    }

    @Override
    public List<Usuario> lista() {
        return usuarioPersistencePort.lista();
    }

    @Override
    public long conta() {
        return usuarioPersistencePort.conta();
    }

    @Override
    public long contaPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula) {
        return usuarioPersistencePort.contaPorNomeOuCrachaOuMatricula(nome, cracha, matricula);
    }

    @Override
    public Usuario buscaPorMatricula(String matricula) {
        return usuarioPersistencePort.buscaPorMatricula(matricula)
                .orElseThrow(() -> new UsuarioInexistenteException("Não existe usuário para matrícula: %s!"
                        .formatted(matricula)));
    }

    @Override
    public Usuario buscaPorId(Integer id) {
        return usuarioPersistencePort.buscaPorId(id).
                orElseThrow(() -> new UsuarioInexistenteException(id));
    }


    @Override
    public Usuario salva(Usuario usuario) {
        var mapCampoMensagem = new HashMap<String, String>();

        var existeCracha = usuarioPersistencePort.checaSeExisteUsuarioComCracha(usuario.getCracha(), usuario.getId());
        var existeMatricula = usuarioPersistencePort.checaSeExisteUsuarioComMatricula(usuario.getMatricula(), usuario.getId());

        if (existeCracha) {
            mapCampoMensagem.put("cracha", "Existe usuário com crachá = " + usuario.getCracha());
        }
        if (existeMatricula) {
            mapCampoMensagem.put("matricula", "Existe usuário com matrícula = " + usuario.getMatricula());
        }

        if (mapCampoMensagem.isEmpty()) {
            return usuarioPersistencePort.salva(usuario);
        }
        throw new CamposUnicosExistentesException(mapCampoMensagem);
    }

    @Override
    public Usuario apagaPorId(Integer id) {
        return usuarioPersistencePort.apagarPorId(id)    ;
    }

    @Override
    public boolean permissaoDiretor() {
        return usuarioSecurityPort.ehDiretor();
    }

    @Override
    public boolean permissaoAdministrador() {
        return usuarioSecurityPort.ehAdmin();
    }

}
