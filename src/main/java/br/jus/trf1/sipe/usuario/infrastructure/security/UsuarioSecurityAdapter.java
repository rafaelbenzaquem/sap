package br.jus.trf1.sipe.usuario.infrastructure.security;

import br.jus.trf1.sipe.usuario.application.web.UsuarioWebMapper;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.domain.port.out.UsuarioSecurityPort;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioNaoAutorizadoException;
import br.jus.trf1.sipe.usuario.infrastructure.db.UsuarioJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class UsuarioSecurityAdapter implements UsuarioSecurityPort {

    private final UsuarioJpaRepository usuarioRepository;
    private Usuario usuario;

    public UsuarioSecurityAdapter(UsuarioJpaRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario getUsuarioAutenticado() {
        if (usuario == null) {
            var matricula = getMatricula();
            return usuarioRepository.findUsuarioByMatricula(matricula)
                    .map(UsuarioWebMapper::toDomain)
                    .orElseThrow(() -> new RuntimeException("Token inválido."));
        }
        return usuario;
    }

    @Override
    public void permissoesNivelUsuario(String matriculaDoRecurso) {
        log.info("Verificando autorização do usuário");
        if (!donoDoRecurso(matriculaDoRecurso) && !ehAdmin() && !ehDiretor() && !ehRH()) {
            throw new UsuarioNaoAutorizadoException("Usuário %s não autorizado a manipular o recurso".formatted(getMatricula()));
        }
    }

    @Override
    public boolean ehDiretor() {
        boolean ehDiretor = temAuthority("GRP_SIPE_DIRETOR");
        log.info("Usuário {}", ehDiretor ? "é diretor" : "não é diretor");
        return ehDiretor;
    }

    @Override
    public boolean ehAdmin() {
        boolean ehAdmin = temAuthority("GRP_SIPE_ADMIN");
        log.info("Usuário {}", ehAdmin ? "é administrador" : "não é administrador");
        return ehAdmin;
    }

    private boolean donoDoRecurso(String matriculaDoRecurso) {
        var matricula = getMatricula();
        var ehDono = matricula.equalsIgnoreCase(matriculaDoRecurso);
        log.info("Usuário {}, {}", matricula, ehDono ? "é dono do recurso" : "não é dono do recurso");
        return ehDono;
    }

    private boolean ehRH() {
        var ehRH = temAuthority("GRP_SIPE_RH");
        log.info("Usuário {}", ehRH ? "é RH" : "não é RH");
        return ehRH;
    }

    private boolean temAuthority(String authority) {
        return getAuthorities().contains(authority);
    }

    private String getMatricula() {
        return getJwt().map(jwt -> jwt.getClaimAsString("sub")).orElseThrow(() -> new RuntimeException("Token inválido."));
    }

    private List<String> getAuthorities() {
        return getJwt()
                .map(jwt -> jwt.getClaimAsStringList("authorities"))
                .orElse(List.of());
    }

    private Optional<Jwt> getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return Optional.of(jwt);
        }
        return Optional.empty();
    }
}