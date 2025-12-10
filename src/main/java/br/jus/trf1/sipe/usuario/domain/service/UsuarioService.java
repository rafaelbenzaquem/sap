package br.jus.trf1.sipe.usuario.domain.service;

import br.jus.trf1.sipe.comum.exceptions.CamposUnicosExistentesException;
import br.jus.trf1.sipe.comum.exceptions.RecursoInvalidoException;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.domain.port.in.UsuarioServicePort;
import br.jus.trf1.sipe.usuario.domain.port.out.UsuarioRepositoryPort;
import br.jus.trf1.sipe.usuario.domain.port.out.UsuarioSecurityPort;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioInexistenteException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class UsuarioService implements UsuarioServicePort {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final UsuarioSecurityPort usuarioSecurityPort;

    public UsuarioService(UsuarioRepositoryPort usuarioRepositoryPort, UsuarioSecurityPort usuarioSecurityPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.usuarioSecurityPort = usuarioSecurityPort;
    }

    @Override
    public Usuario getUsuarioAutenticado() {
        return usuarioSecurityPort.getUsuarioAutenticado();
    }

    @Override
    public void temPermissaoRecurso(Object recurso) {
        if(recurso instanceof Ponto ponto) {
            Objects.requireNonNull(ponto);
            Objects.requireNonNull(ponto.getId());
            Objects.requireNonNull(ponto.getId().getUsuarioJPA().getMatricula());
            usuarioSecurityPort.permissoesNivelUsuario(ponto.getId().getUsuarioJPA().getMatricula());
        }
        throw new RecursoInvalidoException("O objeto %s não é um recurso válido!".formatted(recurso));
    }

    @Override
    public List<Usuario> paginaPorNomeOuCrachaOuMatricula(String nome,
                                                          Integer cracha,
                                                          String matricula,
                                                          int page, int size) {
        return usuarioRepositoryPort.paginaPorNomeOuCrachaOuMatricula(nome, cracha, matricula, page, size);
    }
    @Override
    public List<Usuario> listaPorNomeOuCrachaOuMatricula(String nome,
                                                          Integer cracha,
                                                          String matricula) {
        return usuarioRepositoryPort.listaPorNomeOuCrachaOuMatricula(nome, cracha, matricula);
    }
    @Override
    public List<Usuario> pagina(int page, int size) {
        return usuarioRepositoryPort.pagina(page, size);
    }

    @Override
    public List<Usuario> lista() {
        return usuarioRepositoryPort.lista();
    }

    @Override
    public long conta() {
        return usuarioRepositoryPort.conta();
    }

    @Override
    public long contaPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula) {
        return usuarioRepositoryPort.contaPorNomeOuCrachaOuMatricula(nome, cracha, matricula);
    }

    @Override
    public Usuario buscaPorMatricula(String matricula) {
        return usuarioRepositoryPort.buscaPorMatricula(matricula)
                .orElseThrow(() -> new UsuarioInexistenteException("Não existe usuário para matrícula: %s!"
                        .formatted(matricula)));
    }

    @Override
    public Usuario buscaPorId(Integer id) {
        return usuarioRepositoryPort.buscaPorId(id).
                orElseThrow(() -> new UsuarioInexistenteException(id));
    }


    @Override
    public Usuario salve(Usuario usuario) {
        var mapCampoMensagem = new HashMap<String, String>();

        var existeCracha = usuarioRepositoryPort.checaSeExisteUsuarioComCracha(usuario.getCracha(), usuario.getId());
        var existeMatricula = usuarioRepositoryPort.checaSeExisteUsuarioComMatricula(usuario.getMatricula(), usuario.getId());

        if (existeCracha) {
            mapCampoMensagem.put("cracha", "Existe usuário com crachá = " + usuario.getCracha());
        }
        if (existeMatricula) {
            mapCampoMensagem.put("matricula", "Existe usuário com matrícula = " + usuario.getMatricula());
        }

        if (mapCampoMensagem.isEmpty()) {
            return usuarioRepositoryPort.salva(usuario);
        }
        throw new CamposUnicosExistentesException(mapCampoMensagem);
    }

    @Override
    public Usuario apagaPorId(Integer id) {
        return usuarioRepositoryPort.apagarPorId(id)    ;
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
