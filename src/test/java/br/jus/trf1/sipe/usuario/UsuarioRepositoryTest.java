package br.jus.trf1.sipe.usuario;

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
@Sql(scripts = "classpath:data/usuario/dataset-usuarios.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;

    @Test
    void testFindUsuarioByMatriculaExists() {
        Optional<Usuario> opt = repository.findUsuarioByMatricula("M001");
        assertTrue(opt.isPresent());
        Usuario u = opt.get();
        assertEquals("Alice Wonderland", u.getNome());
        assertEquals("C001", u.getCracha());
        assertEquals(8, u.getHoraDiaria());
    }

    @Test
    void testFindUsuarioByMatriculaNotExists() {
        Optional<Usuario> opt = repository.findUsuarioByMatricula("NONEXIST");
        assertFalse(opt.isPresent());
    }

    @Test
    void testFindAllByNomeOrCrachaOrMatricula() {
        Pageable page = PageRequest.of(0, 10);
        Page<Usuario> p1 = repository.findAllByNomeOrCrachaOrMatricula("ali", null, null, page);
        assertEquals(1, p1.getTotalElements());
        Page<Usuario> p2 = repository.findAllByNomeOrCrachaOrMatricula(null, 200, null, page);
        assertEquals(1, p2.getTotalElements());
        Page<Usuario> p3 = repository.findAllByNomeOrCrachaOrMatricula(null, null, "M002", page);
        assertEquals(1, p3.getTotalElements());
        Page<Usuario> pAll = repository.findAllByNomeOrCrachaOrMatricula(null, null, null, page);
        assertEquals(0, pAll.getTotalElements());
    }

    @Test
    void testChecaSeExisteUsuarioComMatricula() {
        Usuario a = repository.findUsuarioByMatricula("M001").orElseThrow();
        Usuario b = repository.findUsuarioByMatricula("M002").orElseThrow();
        assertFalse(repository.checaSeExisteUsuarioComMatricula("M001", a.getId()));
        assertTrue(repository.checaSeExisteUsuarioComMatricula("M001", b.getId()));
    }

    @Test
    void testChecaSeExisteUsuarioComCracha() {
        Usuario a = repository.findUsuarioByMatricula("M001").orElseThrow();
        Usuario b = repository.findUsuarioByMatricula("M002").orElseThrow();
        assertFalse(repository.checaSeExisteUsuarioComCracha(200, a.getId()));
        assertTrue(repository.checaSeExisteUsuarioComCracha(100, b.getId()));
    }
}
