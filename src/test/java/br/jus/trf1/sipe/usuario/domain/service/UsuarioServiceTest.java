package br.jus.trf1.sipe.usuario.domain.service;

import br.jus.trf1.sipe.comum.exceptions.CamposUnicosExistentesException;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.domain.port.in.UsuarioServicePort;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioInexistenteException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração do UsuarioServicePort.
 * A configuração TestJwtDecoderConfig é detectada automaticamente no perfil "test".
 */
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:data/usuario/dataset-usuarios.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
class UsuarioServiceTest {

    private static final String MATRICULA_ALICE = "M001";
    private static final String MATRICULA_BOB = "M002";
    private static final String AUTHORITY_ADMIN = "GRP_SIPE_ADMIN";
    private static final String AUTHORITY_DIRETOR = "GRP_SIPE_DIRETOR";
    private static final String AUTHORITY_RH = "GRP_SIPE_RH";
    private static final String AUTHORITY_USERS = "GRP_SIPE_USERS";

    @Autowired
    private UsuarioServicePort usuarioServicePort;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Cria um JWT mockado com a matrícula e authorities especificadas
     */
    private Jwt criarJwtMock(String matricula, List<String> authorities) {
        return new Jwt(
                "mock-token-value",
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
     * Configura o SecurityContext com um JWT autenticado
     */
    private void configurarSecurityContext(String matricula, List<String> authorities) {
        Jwt jwt = criarJwtMock(matricula, authorities);
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void paginaDeveRetornarTodosUsuarios() {
        List<Usuario> result = usuarioServicePort.pagina(0, 10);
        assertEquals(2, result.size(), "Deve retornar todos os usuários cadastrado no dataset");
    }

    @Test
    void paginaPorNomeOuCrachaOuMatricula() {
        List<Usuario> p = usuarioServicePort.paginaPorNomeOuCrachaOuMatricula("Alice", null, null, 0, 10);
        assertEquals(1, p.size());
        assertEquals("Alice Wonderland", p.getFirst().getNome());
    }

    @Test
    void buscaPorMatriculaExistente() {
        Usuario u = usuarioServicePort.buscaPorMatricula("M001");
        assertNotNull(u);
        assertEquals("Alice Wonderland", u.getNome());
    }

    @Test
    void buscaPorMatriculaInexistente() {
        UsuarioInexistenteException ex = assertThrows(UsuarioInexistenteException.class,
            () -> usuarioServicePort.buscaPorMatricula("XXX"));
        assertTrue(ex.getMessage().contains("Não existe usuário para matrícula: XXX!"));
    }

    @Test
    void buscaPorIdExistente() {
        Usuario u = usuarioServicePort.buscaPorId(1);
        assertNotNull(u);
    }

    @Test
    void buscaPorIdInexistente() {
        UsuarioInexistenteException ex = assertThrows(UsuarioInexistenteException.class,
            () -> usuarioServicePort.buscaPorId(999));
        assertEquals("Não existe UsuarioJpa com id: 999", ex.getMessage());
    }

    @Test
    void salvaSucesso() {
        Usuario u = usuarioServicePort.buscaPorMatricula("M001");
        assertNotNull(u);
        u.setHoraDiaria(9);
        Usuario updated = usuarioServicePort.salva(u);
        assertEquals(9, updated.getHoraDiaria());
    }

    @Test
    void salveMatriculaDuplicadaLancaExcecao() {
        Usuario bob = usuarioServicePort.buscaPorMatricula("M002");
        assertNotNull(bob);
        bob.setMatricula("M001");
        CamposUnicosExistentesException ex = assertThrows(CamposUnicosExistentesException.class,
            () -> usuarioServicePort.salva(bob));
        assertTrue(ex.getMapCampoUnicoMensagem().containsKey("matricula"));
    }

    @Test
    void salveCrachaDuplicadoLancaExcecao() {
        Usuario bob = usuarioServicePort.buscaPorMatricula("M002");
        bob.setCracha(1001);
        CamposUnicosExistentesException ex = assertThrows(CamposUnicosExistentesException.class,
            () -> usuarioServicePort.salva(bob));
        assertTrue(ex.getMapCampoUnicoMensagem().containsKey("cracha"));
    }

    @Test
    void salveMatriculaECrachaDuplicadosLancaExcecao() {
        Usuario bob = usuarioServicePort.buscaPorMatricula("M002");
        bob.setMatricula("M001");
        bob.setCracha(1001);
        CamposUnicosExistentesException ex = assertThrows(CamposUnicosExistentesException.class,
            () -> usuarioServicePort.salva(bob));
        assertTrue(ex.getMapCampoUnicoMensagem().containsKey("matricula"));
        assertTrue(ex.getMapCampoUnicoMensagem().containsKey("cracha"));
    }

    @Nested
    @DisplayName("Testes de getUsuarioAutenticado")
    @Transactional
    class GetUsuarioAutenticadoTests {

        @Test
        @DisplayName("Deve retornar usuário Alice quando autenticado com matrícula M001")
        void deveRetornarUsuarioAliceQuandoAutenticadoComMatriculaM001() {
            configurarSecurityContext(MATRICULA_ALICE, List.of(AUTHORITY_USERS));

            Usuario usuario = usuarioServicePort.getUsuarioAutenticado();

            assertNotNull(usuario);
            assertEquals(MATRICULA_ALICE, usuario.getMatricula());
            assertEquals("Alice Wonderland", usuario.getNome());
        }

        @Test
        @DisplayName("Deve retornar usuário Bob quando autenticado com matrícula M002")
        void deveRetornarUsuarioBobQuandoAutenticadoComMatriculaM002() {
            configurarSecurityContext(MATRICULA_BOB, List.of(AUTHORITY_USERS));

            Usuario usuario = usuarioServicePort.getUsuarioAutenticado();

            assertNotNull(usuario);
            assertEquals(MATRICULA_BOB, usuario.getMatricula());
            assertEquals("Bob Builder", usuario.getNome());
        }

        @Test
        @DisplayName("Deve lançar exceção quando token não contém matrícula válida no banco")
        void deveLancarExcecaoQuandoMatriculaNaoExisteNoBanco() {
            configurarSecurityContext("MATRICULA_INEXISTENTE", List.of(AUTHORITY_USERS));

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> usuarioServicePort.getUsuarioAutenticado());

            assertEquals("Token inválido.", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando não há autenticação no contexto")
        void deveLancarExcecaoQuandoNaoHaAutenticacao() {
            SecurityContextHolder.clearContext();

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> usuarioServicePort.getUsuarioAutenticado());

            assertEquals("Token inválido.", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Testes de permissaoDiretor")
    @Transactional
    class PermissaoDiretorTests {

        @Test
        @DisplayName("Deve retornar true quando usuário possui authority GRP_SIPE_DIRETOR")
        void deveRetornarTrueQuandoUsuarioPossuiAuthorityDiretor() {
            configurarSecurityContext(MATRICULA_ALICE, List.of(AUTHORITY_DIRETOR));

            boolean resultado = usuarioServicePort.permissaoDiretor();

            assertTrue(resultado);
        }

        @Test
        @DisplayName("Deve retornar false quando usuário não possui authority GRP_SIPE_DIRETOR")
        void deveRetornarFalseQuandoUsuarioNaoPossuiAuthorityDiretor() {
            configurarSecurityContext(MATRICULA_ALICE, List.of(AUTHORITY_USERS));

            boolean resultado = usuarioServicePort.permissaoDiretor();

            assertFalse(resultado);
        }

        @Test
        @DisplayName("Deve retornar true quando usuário possui múltiplas authorities incluindo DIRETOR")
        void deveRetornarTrueQuandoUsuarioPossuiMultiplasAuthoritiesIncluindoDiretor() {
            configurarSecurityContext(MATRICULA_ALICE, List.of(AUTHORITY_USERS, AUTHORITY_DIRETOR, AUTHORITY_RH));

            boolean resultado = usuarioServicePort.permissaoDiretor();

            assertTrue(resultado);
        }

        @Test
        @DisplayName("Deve retornar false quando lista de authorities está vazia")
        void deveRetornarFalseQuandoListaDeAuthoritiesVazia() {
            configurarSecurityContext(MATRICULA_ALICE, List.of());

            boolean resultado = usuarioServicePort.permissaoDiretor();

            assertFalse(resultado);
        }
    }

    @Nested
    @DisplayName("Testes de permissaoAdministrador")
    @Transactional
    class PermissaoAdministradorTests {

        @Test
        @DisplayName("Deve retornar true quando usuário possui authority GRP_SIPE_ADMIN")
        void deveRetornarTrueQuandoUsuarioPossuiAuthorityAdmin() {
            configurarSecurityContext(MATRICULA_ALICE, List.of(AUTHORITY_ADMIN));

            boolean resultado = usuarioServicePort.permissaoAdministrador();

            assertTrue(resultado);
        }

        @Test
        @DisplayName("Deve retornar false quando usuário não possui authority GRP_SIPE_ADMIN")
        void deveRetornarFalseQuandoUsuarioNaoPossuiAuthorityAdmin() {
            configurarSecurityContext(MATRICULA_ALICE, List.of(AUTHORITY_USERS));

            boolean resultado = usuarioServicePort.permissaoAdministrador();

            assertFalse(resultado);
        }

        @Test
        @DisplayName("Deve retornar true quando usuário possui múltiplas authorities incluindo ADMIN")
        void deveRetornarTrueQuandoUsuarioPossuiMultiplasAuthoritiesIncluindoAdmin() {
            configurarSecurityContext(MATRICULA_BOB, List.of(AUTHORITY_USERS, AUTHORITY_ADMIN));

            boolean resultado = usuarioServicePort.permissaoAdministrador();

            assertTrue(resultado);
        }

        @Test
        @DisplayName("Deve retornar false quando lista de authorities está vazia")
        void deveRetornarFalseQuandoListaDeAuthoritiesVazia() {
            configurarSecurityContext(MATRICULA_BOB, List.of());

            boolean resultado = usuarioServicePort.permissaoAdministrador();

            assertFalse(resultado);
        }
    }

    @Nested
    @DisplayName("Testes de cenários combinados de authorities")
    @Transactional
    class CenariosCombnadosAuthoritiesTests {

        @Test
        @DisplayName("Usuário com todas as authorities deve ter permissão de diretor e admin")
        void usuarioComTodasAuthoritiesDeveTerPermissaoDiretorEAdmin() {
            configurarSecurityContext(MATRICULA_ALICE,
                    List.of(AUTHORITY_ADMIN, AUTHORITY_DIRETOR, AUTHORITY_RH, AUTHORITY_USERS));

            assertTrue(usuarioServicePort.permissaoDiretor());
            assertTrue(usuarioServicePort.permissaoAdministrador());
        }

        @Test
        @DisplayName("Usuário apenas com authority USERS não deve ter permissões especiais")
        void usuarioApenasComAuthorityUsersNaoDeveTerPermissoesEspeciais() {
            configurarSecurityContext(MATRICULA_BOB, List.of(AUTHORITY_USERS));

            assertFalse(usuarioServicePort.permissaoDiretor());
            assertFalse(usuarioServicePort.permissaoAdministrador());
        }

        @Test
        @DisplayName("Usuário com RH apenas não deve ter permissão de diretor ou admin")
        void usuarioComRHApenasNaoDeveTerPermissaoDiretorOuAdmin() {
            configurarSecurityContext(MATRICULA_ALICE, List.of(AUTHORITY_RH));

            assertFalse(usuarioServicePort.permissaoDiretor());
            assertFalse(usuarioServicePort.permissaoAdministrador());
        }

        @Test
        @DisplayName("Deve conseguir obter usuário autenticado e verificar permissões na mesma sessão")
        void deveConseguirObterUsuarioAutenticadoEVerificarPermissoesNaMesmaSessao() {
            configurarSecurityContext(MATRICULA_ALICE, List.of(AUTHORITY_DIRETOR));

            Usuario usuario = usuarioServicePort.getUsuarioAutenticado();
            boolean ehDiretor = usuarioServicePort.permissaoDiretor();
            boolean ehAdmin = usuarioServicePort.permissaoAdministrador();

            assertNotNull(usuario);
            assertEquals(MATRICULA_ALICE, usuario.getMatricula());
            assertTrue(ehDiretor);
            assertFalse(ehAdmin);
        }
    }
}