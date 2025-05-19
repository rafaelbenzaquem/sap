package br.jus.trf1.sipe.registro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:data/registro/dataset-registros.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RegistroRepositoryTest {

    @Autowired
    private RegistroRepository repository;

    @Test
    void testListarRegistrosAtuaisAtivosDoPonto() {
        LocalDate dia = LocalDate.of(2023, 1, 1);
        List<Registro> list = repository.listarRegistrosAtuaisAtivosDoPonto("M1", dia);
        assertEquals(1, list.size());
        assertEquals(100, list.getFirst().getCodigoAcesso().intValue());
    }

    @Test
    void testListarRegistrosHistoricosDoPonto() {
        LocalDate dia = LocalDate.of(2023, 1, 1);
        List<Registro> list = repository.listarRegistrosHistoricosDoPonto("M1", dia);
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(r -> r.getCodigoAcesso() == 100));
        assertTrue(list.stream().anyMatch(r -> r.getCodigoAcesso() == 101));
    }

    @Test
    void testFindByCodigoAcessoExists() {
        Optional<Registro> opt = repository.findByCodigoAcesso(100);
        assertTrue(opt.isPresent());
        assertEquals(1L, opt.get().getId().longValue());
    }

    @Test
    void testFindByCodigoAcessoNotExists() {
        Optional<Registro> opt = repository.findByCodigoAcesso(999);
        assertFalse(opt.isPresent());
    }
}