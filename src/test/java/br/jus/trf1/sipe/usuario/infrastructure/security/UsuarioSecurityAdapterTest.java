package br.jus.trf1.sipe.usuario.infrastructure.security;

import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioNaoAutorizadoException;
import br.jus.trf1.sipe.usuario.infrastructure.jpa.UsuarioJpa;
import br.jus.trf1.sipe.usuario.infrastructure.jpa.UsuarioJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Testes do UsuarioSecurityAdapter")
class UsuarioSecurityAdapterTest {

    @Mock
    private UsuarioJpaRepository usuarioRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UsuarioSecurityAdapter usuarioSecurityAdapter;

    private Jwt jwt;
    private static final String MATRICULA_TESTE = "12345";
    private static final String MATRICULA_OUTRO = "67890";

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Método auxiliar para criar um JWT mockado
     */
    private Jwt criarJwtMock(String matricula, List<String> authorities) {
        return new Jwt(
                "token",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "RS256"),
                Map.of(
                        "sub", matricula,
                        "authorities", authorities
                )
        );
    }

    /**
     * Método auxiliar para configurar o contexto de segurança
     */
    private void configurarSecurityContext(Jwt jwt) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
    }

    @Nested
    @DisplayName("Testes de getUsuarioAutenticado")
    class GetUsuarioAutenticadoTests {

        @Test
        @DisplayName("Deve retornar usuário quando encontrado no repositório")
        void deveRetornarUsuarioQuandoEncontrado() {
            // Arrange
            jwt = criarJwtMock(MATRICULA_TESTE, List.of());
            configurarSecurityContext(jwt);

            UsuarioJpa usuarioEntity = new UsuarioJpa();
            usuarioEntity.setMatricula(MATRICULA_TESTE);

            when(usuarioRepository.findUsuarioByMatricula(MATRICULA_TESTE))
                    .thenReturn(Optional.of(usuarioEntity));

            // Act
            Usuario resultado = usuarioSecurityAdapter.getUsuarioAutenticado();

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getMatricula()).isEqualTo(MATRICULA_TESTE);
            verify(usuarioRepository).findUsuarioByMatricula(MATRICULA_TESTE);
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário não encontrado")
        void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
            // Arrange
            jwt = criarJwtMock(MATRICULA_TESTE, List.of());
            configurarSecurityContext(jwt);

            when(usuarioRepository.findUsuarioByMatricula(MATRICULA_TESTE))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> usuarioSecurityAdapter.getUsuarioAutenticado())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Token inválido.");

            verify(usuarioRepository).findUsuarioByMatricula(MATRICULA_TESTE);
        }

        @Test
        @DisplayName("Deve lançar exceção quando não há autenticação")
        void deveLancarExcecaoQuandoNaoHaAutenticacao() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(null);

            // Act & Assert
            assertThatThrownBy(() -> usuarioSecurityAdapter.getUsuarioAutenticado())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Token inválido.");

            verify(usuarioRepository, never()).findUsuarioByMatricula(anyString());
        }

        @Test
        @DisplayName("Deve lançar exceção quando principal não é JWT")
        void deveLancarExcecaoQuandoPrincipalNaoEhJwt() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn("not-a-jwt");

            // Act & Assert
            assertThatThrownBy(() -> usuarioSecurityAdapter.getUsuarioAutenticado())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Token inválido.");
        }
    }

    @Nested
    @DisplayName("Testes de verificação de permissões")
    class PermissoesTests {

        @Test
        @DisplayName("Deve permitir quando usuário é dono do recurso")
        void devePermitirQuandoUsuarioEhDonoDoRecurso() {
            // Arrange
            jwt = criarJwtMock(MATRICULA_TESTE, List.of());
            configurarSecurityContext(jwt);

            // Act & Assert
            usuarioSecurityAdapter.permissoesNivelUsuario(MATRICULA_TESTE);
            // Não deve lançar exceção
        }

        @Test
        @DisplayName("Deve permitir quando usuário é admin")
        void devePermitirQuandoUsuarioEhAdmin() {
            // Arrange
            jwt = criarJwtMock(MATRICULA_TESTE, List.of("GRP_SIPE_ADMIN"));
            configurarSecurityContext(jwt);

            // Act & Assert
            usuarioSecurityAdapter.permissoesNivelUsuario(MATRICULA_OUTRO);
            // Não deve lançar exceção
        }

        @Test
        @DisplayName("Deve permitir quando usuário é diretor")
        void devePermitirQuandoUsuarioEhDiretor() {
            // Arrange
            jwt = criarJwtMock(MATRICULA_TESTE, List.of("GRP_SIPE_DIRETOR"));
            configurarSecurityContext(jwt);

            // Act & Assert
            usuarioSecurityAdapter.permissoesNivelUsuario(MATRICULA_OUTRO);
            // Não deve lançar exceção
        }

        @Test
        @DisplayName("Deve permitir quando usuário é RH")
        void devePermitirQuandoUsuarioEhRH() {
            // Arrange
            jwt = criarJwtMock(MATRICULA_TESTE, List.of("GRP_SIPE_RH"));
            configurarSecurityContext(jwt);

            // Act & Assert
            usuarioSecurityAdapter.permissoesNivelUsuario(MATRICULA_OUTRO);
            // Não deve lançar exceção
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário não tem permissão")
        void deveLancarExcecaoQuandoUsuarioNaoTemPermissao() {
            // Arrange
            jwt = criarJwtMock(MATRICULA_TESTE, List.of());
            configurarSecurityContext(jwt);

            // Act & Assert
            assertThatThrownBy(() ->
                    usuarioSecurityAdapter.permissoesNivelUsuario(MATRICULA_OUTRO))
                    .isInstanceOf(UsuarioNaoAutorizadoException.class)
                    .hasMessageContaining("não autorizado a manipular o recurso");
        }

        @Test
        @DisplayName("Deve ser case insensitive ao verificar dono do recurso")
        void deveSerCaseInsensitiveAoVerificarDonoDoRecurso() {
            // Arrange
            jwt = criarJwtMock("abc123", List.of());
            configurarSecurityContext(jwt);

            // Act & Assert
            usuarioSecurityAdapter.permissoesNivelUsuario("ABC123");
            // Não deve lançar exceção
        }
    }

    @Nested
    @DisplayName("Testes de verificação de papéis")
    class VerificacaoPapeisTests {

        @Test
        @DisplayName("Deve retornar true quando usuário é diretor")
        void deveRetornarTrueQuandoUsuarioEhDiretor() {
            // Arrange
            jwt = criarJwtMock(MATRICULA_TESTE, List.of("GRP_SIPE_DIRETOR"));
            configurarSecurityContext(jwt);

            // Act
            boolean resultado = usuarioSecurityAdapter.ehDiretor();

            // Assert
            assertThat(resultado).isTrue();
        }

        @Test
        @DisplayName("Deve retornar false quando usuário não é diretor")
        void deveRetornarFalseQuandoUsuarioNaoEhDiretor() {
            // Arrange
            jwt = criarJwtMock(MATRICULA_TESTE, List.of("OUTRA_ROLE"));
            configurarSecurityContext(jwt);

            // Act
            boolean resultado = usuarioSecurityAdapter.ehDiretor();

            // Assert
            assertThat(resultado).isFalse();
        }

        @Test
        @DisplayName("Deve retornar true quando usuário é admin")
        void deveRetornarTrueQuandoUsuarioEhAdmin() {
            // Arrange
            jwt = criarJwtMock(MATRICULA_TESTE, List.of("GRP_SIPE_ADMIN"));
            configurarSecurityContext(jwt);

            // Act
            boolean resultado = usuarioSecurityAdapter.ehAdmin();

            // Assert
            assertThat(resultado).isTrue();
        }

        @Test
        @DisplayName("Deve retornar false quando usuário não é admin")
        void deveRetornarFalseQuandoUsuarioNaoEhAdmin() {
            // Arrange
            jwt = criarJwtMock(MATRICULA_TESTE, List.of());
            configurarSecurityContext(jwt);

            // Act
            boolean resultado = usuarioSecurityAdapter.ehAdmin();

            // Assert
            assertThat(resultado).isFalse();
        }

        @Test
        @DisplayName("Deve funcionar com múltiplas authorities")
        void deveFuncionarComMultiplasAuthorities() {
            // Arrange
            jwt = criarJwtMock(MATRICULA_TESTE,
                    List.of("GRP_SIPE_ADMIN", "GRP_SIPE_DIRETOR", "GRP_SIPE_RH"));
            configurarSecurityContext(jwt);

            // Act & Assert
            assertThat(usuarioSecurityAdapter.ehAdmin()).isTrue();
            assertThat(usuarioSecurityAdapter.ehDiretor()).isTrue();
        }
    }

    @Nested
    @DisplayName("Testes de cenários edge case")
    class EdgeCaseTests {

        @Test
        @DisplayName("Deve lidar com lista de authorities vazia")
        void deveLidarComListaDeAuthoritiesVazia() {
            // Arrange
            jwt = criarJwtMock(MATRICULA_TESTE, List.of());
            configurarSecurityContext(jwt);

            // Act & Assert
            assertThat(usuarioSecurityAdapter.ehAdmin()).isFalse();
            assertThat(usuarioSecurityAdapter.ehDiretor()).isFalse();
        }

        @Test
        @DisplayName("Deve lidar com authorities null retornando lista vazia")
        void deveLidarComAuthoritiesNull() {
            // Arrange
            jwt = new Jwt(
                    "token",
                    Instant.now(),
                    Instant.now().plusSeconds(3600),
                    Map.of("alg", "RS256"),
                    Map.of("sub", MATRICULA_TESTE) // sem authorities
            );
            configurarSecurityContext(jwt);

            // Act & Assert
            assertThat(usuarioSecurityAdapter.ehAdmin()).isFalse();
            assertThat(usuarioSecurityAdapter.ehDiretor()).isFalse();
        }
    }
}