package br.jus.trf1.sipe.usuario;

import br.jus.trf1.sipe.usuario.exceptions.UsuarioNaoAutorizadoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class UsuarioAtualService {

    private final UsuarioRepository usuarioRepository;

    private Usuario usuario;

    public UsuarioAtualService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void permissoesNivelUsuario(String matriculaDoRecurso) {
        log.info("Verificando autorização do usuário");

        var temPermissaoNoRecuso = donoDoRecurso(matriculaDoRecurso) || ehAdmin() || ehDiretor() || ehRH();
        if (temPermissaoNoRecuso) {
            return;
        }
        throw new UsuarioNaoAutorizadoException("Usuário %s não autorizado a manipular o recurso".formatted(getMatricula()));
    }

    public boolean donoDoRecurso(String matriculaDoRecurso) {
        var matricula = getMatricula();
        var ehDono = matricula.equalsIgnoreCase(matriculaDoRecurso);
        log.info("Usuário {}, {}", matricula, ehDono ? "é dono do recurso" : "não é dono do recurso");
        return ehDono;
    }

    public boolean ehAdmin() {
        var ehAdmin = temAuthority("GRP_SIPE_ADMIN");
        log.info("Usuário {}", ehAdmin ? "é administrador" : "não é administrador");
        return ehAdmin;
    }

    public boolean ehDiretor() {
        var ehDiretor = temAuthority("GRP_SIPE_DIRETOR");
        log.info("Usuário {}", ehDiretor ? "é diretor" : "não é diretor");
        return ehDiretor;
    }

    public boolean ehRH() {
        var ehRH = temAuthority("GRP_SIPE_RH");
        log.info("Usuário {}", ehRH ? "é RH" : "não é RH");
        return ehRH;
    }

    private boolean temAuthority(String authority) {
        var authoritiesList = getAuthorities();
        if (authoritiesList.isEmpty()) {
            return false;
        }
        return authoritiesList.contains(authority);
    }

    public Map<String, Object> detalharUsuario() {
        var detalhes = new HashMap<String, Object>();
        detalhes.put("usuario", usuario);
        detalhes.put("matricula", getMatricula());
        detalhes.put("authorities", getAuthorities());
        return detalhes;
    }

    public Usuario getUsuario() {
        if (usuario == null) {
            var matricula = getMatricula();
            return usuarioRepository.findUsuarioByMatricula(matricula).orElseThrow(() -> new RuntimeException("Token inválido."));
        }
        return usuario;
    }

    public String getMatricula() {
        return getJwt().map(jwt -> jwt.getClaimAsString("sub")).orElseThrow(() -> new RuntimeException("Token inválido."));
    }

    public List<String> getAuthorities() {
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
