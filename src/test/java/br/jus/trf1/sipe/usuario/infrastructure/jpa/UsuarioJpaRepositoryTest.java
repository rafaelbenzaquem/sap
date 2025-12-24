package br.jus.trf1.sipe.usuario.infrastructure.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:data/usuario/dataset-usuarios.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UsuarioJpaRepositoryTest {

    @Autowired
    private UsuarioJpaRepository usuarioPersistencePort;

    @Test
    void testFindUsuarioByMatriculaExists() {
        Optional<UsuarioJpa> opt = usuarioPersistencePort.findUsuarioByMatricula("M001");
        assertTrue(opt.isPresent());
        UsuarioJpa u = opt.get();
        assertEquals("Alice Wonderland", u.getNome());
        assertEquals(1001, u.getCracha());
        assertEquals(8, u.getHoraDiaria());
    }

    @Test
    void testFindUsuarioByMatriculaNotExists() {
        Optional<UsuarioJpa> opt = usuarioPersistencePort.findUsuarioByMatricula("NONEXIST");
        assertFalse(opt.isPresent());
    }

    @Test
    void testPaginaPorNomeOuCrachaOuMatricula() {
        Pageable page = PageRequest.of(0, 10);
        Page<UsuarioJpa> p1 = usuarioPersistencePort.paginaPorNomeOuCrachaOuMatricula("ali", null, null, page);
        assertEquals(1, p1.getTotalElements());
        Page<UsuarioJpa> p2 = usuarioPersistencePort.paginaPorNomeOuCrachaOuMatricula(null, 1001, null, page);
        assertEquals(1, p2.getTotalElements());
        Page<UsuarioJpa> p3 = usuarioPersistencePort.paginaPorNomeOuCrachaOuMatricula(null, null, "M002", page);
        assertEquals(1, p3.getTotalElements());
        Page<UsuarioJpa> pAll = usuarioPersistencePort.paginaPorNomeOuCrachaOuMatricula(null, null, null, page);
        assertEquals(0, pAll.getTotalElements());
    }

    @Test
    void testChecaSeExisteUsuarioComMatricula() {
        UsuarioJpa a = usuarioPersistencePort.findUsuarioByMatricula("M001").orElseThrow();
        UsuarioJpa b = usuarioPersistencePort.findUsuarioByMatricula("M002").orElseThrow();
        assertFalse(usuarioPersistencePort.checaSeExisteUsuarioComMatricula("M001", a.getId()));
        assertTrue(usuarioPersistencePort.checaSeExisteUsuarioComMatricula("M001", b.getId()));
    }

    @Test
    void testChecaSeExisteUsuarioComCracha() {
        UsuarioJpa a = usuarioPersistencePort.findUsuarioByMatricula("M001").orElseThrow();
        UsuarioJpa b = usuarioPersistencePort.findUsuarioByMatricula("M002").orElseThrow();
        assertFalse(usuarioPersistencePort.checaSeExisteUsuarioComCracha(200, a.getId()));
        assertTrue(usuarioPersistencePort.checaSeExisteUsuarioComCracha(1001, b.getId()));
    }
}
