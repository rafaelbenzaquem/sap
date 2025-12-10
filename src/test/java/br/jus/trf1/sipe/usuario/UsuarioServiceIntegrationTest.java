package br.jus.trf1.sipe.usuario;

import br.jus.trf1.sipe.comum.exceptions.CamposUnicosExistentesException;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.domain.service.UsuarioService;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioInexistenteException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:data/usuario/dataset-usuarios.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UsuarioServiceIntegrationTest {

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void paginaDeveRetornarTodosUsuarios() {
        List<Usuario> result = usuarioService.pagina(0, 10);
        assertEquals(2, result.size(), "Deve retornar todos os usuários cadastrado no dataset");
    }

    @Test
    void paginaPorNomeOuCrachaOuMatricula() {
        List<Usuario> p = usuarioService.paginaPorNomeOuCrachaOuMatricula("Alice", null, null, 0, 10);
        assertEquals(1, p.size());
        assertEquals("Alice Wonderland", p.getFirst().getNome());
    }

    @Test
    void buscaPorMatriculaExistente() {
        Usuario u = usuarioService.buscaPorMatricula("M001");
        assertNotNull(u);
        assertEquals("Alice Wonderland", u.getNome());
    }

    @Test
    void buscaPorMatriculaInexistente() {
        UsuarioInexistenteException ex = assertThrows(UsuarioInexistenteException.class,
            () -> usuarioService.buscaPorMatricula("XXX"));
        assertTrue(ex.getMessage().contains("Não existe usuário para matrícula: XXX!"));
    }

    @Test
    void buscaPorIdExistente() {
        Usuario u = usuarioService.buscaPorId(1);
        assertNotNull(u);
    }

    @Test
    void buscaPorIdInexistente() {
        UsuarioInexistenteException ex = assertThrows(UsuarioInexistenteException.class,
            () -> usuarioService.buscaPorId(999));
        assertEquals("Não existe UsuarioJpa com id: 999", ex.getMessage());
    }

    @Test
    void salveSucesso() {
        Usuario u = usuarioService.buscaPorMatricula("M001");
        assertNotNull(u);
        u.setHoraDiaria(9);
        Usuario updated = usuarioService.salve(u);
        assertEquals(9, updated.getHoraDiaria());
    }

    @Test
    void salveMatriculaDuplicadaLancaExcecao() {
        Usuario bob = usuarioService.buscaPorMatricula("M002");
        assertNotNull(bob);
        bob.setMatricula("M001");
        CamposUnicosExistentesException ex = assertThrows(CamposUnicosExistentesException.class,
            () -> usuarioService.salve(bob));
        assertTrue(ex.getMapCampoUnicoMensagem().containsKey("matricula"));
    }

    @Test
    void salveCrachaDuplicadoLancaExcecao() {
        Usuario bob = usuarioService.buscaPorMatricula("M002");
        bob.setCracha(100);
        CamposUnicosExistentesException ex = assertThrows(CamposUnicosExistentesException.class,
            () -> usuarioService.salve(bob));
        assertTrue(ex.getMapCampoUnicoMensagem().containsKey("cracha"));
    }

    @Test
    void salveMatriculaECrachaDuplicadosLancaExcecao() {
        Usuario bob = usuarioService.buscaPorMatricula("M002");
        bob.setMatricula("M001");
        bob.setCracha(100);
        CamposUnicosExistentesException ex = assertThrows(CamposUnicosExistentesException.class,
            () -> usuarioService.salve(bob));
        assertTrue(ex.getMapCampoUnicoMensagem().containsKey("matricula"));
        assertTrue(ex.getMapCampoUnicoMensagem().containsKey("cracha"));
    }
}