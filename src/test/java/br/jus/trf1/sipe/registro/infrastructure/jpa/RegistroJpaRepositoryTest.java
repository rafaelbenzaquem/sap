package br.jus.trf1.sipe.registro.infrastructure.jpa;

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
@Sql(scripts = {"classpath:data/usuario/dataset-usuarios.sql", "classpath:data/ponto/dataset-pontos.sql",
        "classpath:data/registro/dataset-registros.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RegistroJpaRepositoryTest {

    @Autowired
    private RegistroJpaRepository repository;

    @Test
    void testListaRegistrosAtuaisAtivosDoPonto() {
        LocalDate dia = LocalDate.of(2023, 1, 1);
        List<RegistroJpa> list = repository.listaRegistrosAtuaisAtivosDoPonto("M001", dia);
        assertEquals(1, list.size());
        assertEquals(100, list.getFirst().getCodigoAcesso().intValue());
    }

    @Test
    void testListarRegistrosProvenientesDoSistemaExterno() {
        LocalDate dia = LocalDate.of(2023, 1, 1);
        List<RegistroJpa> list = repository.listaProvenientesDoSistemaExterno("M001", dia);
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(r -> r.getCodigoAcesso() == 100));
        assertTrue(list.stream().anyMatch(r -> r.getCodigoAcesso() == 101));
    }

    @Test
    void testFindByCodigoAcessoExists() {
        Optional<RegistroJpa> opt = repository.findByCodigoAcesso(100);
        assertTrue(opt.isPresent());
        assertEquals(1L, opt.get().getId().longValue());
    }

    @Test
    void testFindByCodigoAcessoNotExists() {
        Optional<RegistroJpa> opt = repository.findByCodigoAcesso(999);
        assertFalse(opt.isPresent());
    }
}