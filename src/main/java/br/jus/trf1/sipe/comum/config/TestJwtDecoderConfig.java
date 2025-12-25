package br.jus.trf1.sipe.comum.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Configuração de JwtDecoder para testes.
 * Fornece um JwtDecoder mock que aceita tokens no formato simplificado de teste.
 * Ativado apenas no perfil "test".
 *
 * <p>Formato do token de teste: "matricula|authority1,authority2,..."</p>
 * <p>Exemplo: "M001|GRP_SIPE_ADMIN,GRP_SIPE_USERS"</p>
 */
@Configuration
@Profile("test")
public class TestJwtDecoderConfig {

    private static final Logger log = LoggerFactory.getLogger(TestJwtDecoderConfig.class);
    public static final String TOKEN_SEPARATOR = "|";
    public static final String AUTHORITY_SEPARATOR = ",";

    @Bean
    @Primary
    public JwtDecoder jwtDecoder() {
        log.info("TestJwtDecoderConfig: Criando bean JwtDecoder de teste...");
        return new TestJwtDecoder();
    }

    /**
     * Implementação interna imperativa do JwtDecoder para facilitar debugging.
     */
    public static class TestJwtDecoder implements JwtDecoder {

        public TestJwtDecoder() {
            log.info("TestJwtDecoder: Instância criada.");
        }

        @Override
        public Jwt decode(String token) throws JwtException {
            try {
                log.info("TestJwtDecoder: Iniciando decodificação do token: {}", token);

                String matricula;
                List<String> authorities;

                if (token == null) {
                    throw new IllegalArgumentException("Token não pode ser nulo");
                }

                // Verifica se o token contém o separador
                if (token.contains(TOKEN_SEPARATOR)) {
                    String[] parts = token.split(Pattern.quote(TOKEN_SEPARATOR));
                    matricula = parts[0];
                    
                    if (parts.length > 1 && !parts[1].isBlank()) {
                        String authoritiesString = parts[1];
                        String[] authoritiesArray = authoritiesString.split(Pattern.quote(AUTHORITY_SEPARATOR));
                        authorities = Arrays.asList(authoritiesArray);
                    } else {
                        authorities = Collections.emptyList();
                    }
                } else {
                    // Se não tiver separador, assume que é apenas a matrícula
                    matricula = token;
                    authorities = Collections.emptyList();
                }

                log.info("TestJwtDecoder: Dados extraídos - Matricula: {}, Authorities: {}", matricula, authorities);

                Instant now = Instant.now();
                Instant issuedAt = now.minusSeconds(60); // Recua 1 min para evitar erro de clock skew
                Instant expiresAt = now.plusSeconds(3600);

                Jwt jwt = Jwt.withTokenValue(token)
                        .header("alg", "none")
                        .header("typ", "JWT")
                        .subject(matricula)
                        .claim("authorities", authorities)
                        .claim("scope", authorities) // Adiciona scope para compatibilidade
                        .issuedAt(issuedAt)
                        .expiresAt(expiresAt)
                        .build();
                
                log.info("TestJwtDecoder: JWT construído com sucesso: {}", jwt.getClaims());
                
                return jwt;

            } catch (Exception e) {
                log.error("TestJwtDecoder: Erro ao decodificar token: " + token, e);
                throw new JwtException("Erro ao decodificar token de teste: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Cria um token de teste com a matrícula e authorities especificadas.
     */
    public static String criarTokenTeste(String matricula, String... authorities) {
        if (authorities == null || authorities.length == 0) {
            return matricula + TOKEN_SEPARATOR;
        }
        return matricula + TOKEN_SEPARATOR + String.join(AUTHORITY_SEPARATOR, authorities);
    }
}
