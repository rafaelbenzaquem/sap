package br.jus.trf1.sipe.usuario;

import br.jus.trf1.sipe.comum.exceptions.CamposUnicosExistentesException;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioInexistenteException;
import br.jus.trf1.sipe.usuario.infrastructure.persistence.UsuarioJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:data/usuario/dataset-usuarios.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UsuarioJpaServiceIntegrationTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void listarDeveRetornarTodosUsuarios() {
        Pageable page = PageRequest.of(0, 10);
        Page<UsuarioJpa> result = usuarioService.listar(page);
        assertEquals(2, result.getTotalElements(), "Deve retornar todos os usuários cadastrado no dataset");
    }

    @Test
    void buscaPorNomeOuCrachaOuMatricula() {
        Pageable page = PageRequest.of(0, 10);
        Page<UsuarioJpa> p = usuarioService.buscaPorNomeOuCrachaOuMatricula("Alice", null, null, page);
        assertEquals(1, p.getTotalElements());
        assertEquals("Alice Wonderland", p.getContent().getFirst().getNome());
    }

    @Test
    void buscaPorMatriculaExistente() {
        UsuarioJpa u = usuarioService.buscaPorMatricula("M001");
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
        UsuarioJpa a = usuarioRepository.findUsuarioByMatricula("M001").orElseThrow();
        UsuarioJpa u = usuarioService.buscaPorId(a.getId());
        assertEquals(a.getId(), u.getId());
    }

    @Test
    void buscaPorIdInexistente() {
        UsuarioInexistenteException ex = assertThrows(UsuarioInexistenteException.class,
            () -> usuarioService.buscaPorId(999));
        assertEquals("Não existe UsuarioJpa com id: 999", ex.getMessage());
    }

    @Test
    void salveSucesso() {
        UsuarioJpa u = usuarioRepository.findUsuarioByMatricula("M001").orElseThrow();
        u.setHoraDiaria(9);
        UsuarioJpa updated = usuarioService.salve(u);
        assertEquals(9, updated.getHoraDiaria());
        UsuarioJpa fromDb = usuarioRepository.findById(u.getId()).orElseThrow();
        assertEquals(9, fromDb.getHoraDiaria());
    }

    @Test
    void salveMatriculaDuplicadaLancaExcecao() {
        UsuarioJpa bob = usuarioRepository.findUsuarioByMatricula("M002").orElseThrow();
        bob.setMatricula("M001");
        CamposUnicosExistentesException ex = assertThrows(CamposUnicosExistentesException.class,
            () -> usuarioService.salve(bob));
        assertTrue(ex.getMapCampoUnicoMensagem().containsKey("matricula"));
    }

    @Test
    void salveCrachaDuplicadoLancaExcecao() {
        UsuarioJpa bob = usuarioRepository.findUsuarioByMatricula("M002").orElseThrow();
        bob.setCracha(100);
        CamposUnicosExistentesException ex = assertThrows(CamposUnicosExistentesException.class,
            () -> usuarioService.salve(bob));
        assertTrue(ex.getMapCampoUnicoMensagem().containsKey("cracha"));
    }

    @Test
    void salveMatriculaECrachaDuplicadosLancaExcecao() {
        UsuarioJpa bob = usuarioRepository.findUsuarioByMatricula("M002").orElseThrow();
        bob.setMatricula("M001");
        bob.setCracha(100);
        CamposUnicosExistentesException ex = assertThrows(CamposUnicosExistentesException.class,
            () -> usuarioService.salve(bob));
        assertTrue(ex.getMapCampoUnicoMensagem().containsKey("matricula"));
        assertTrue(ex.getMapCampoUnicoMensagem().containsKey("cracha"));
    }
}