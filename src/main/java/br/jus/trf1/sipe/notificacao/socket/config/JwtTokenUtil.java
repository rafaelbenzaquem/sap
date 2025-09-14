package br.jus.trf1.sipe.notificacao.socket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class JwtTokenUtil {

    public Optional<Jwt> getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken) authentication;
            return Optional.of(jwtAuthentication.getToken());
        }

        return Optional.empty();
    }

    public String getUsernameFromToken() {
        return getCurrentJwt()
                .map(jwt -> jwt.getClaimAsString("sub"))
                .orElse(null);
    }

    public String getUsernameFromAuthentication(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            return jwtAuth.getToken().getClaimAsString("sub");
        }
        return authentication.getName();
    }

    public boolean isTokenValid() {
        // O Spring Security já validou o token via JWKS ou introspection
        // Podemos verificar se há autenticação presente
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public String getTokenValue() {
        return getCurrentJwt()
                .map(Jwt::getTokenValue)
                .orElse(null);
    }
}