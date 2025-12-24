package br.jus.trf1.sipe.arquivo.infrastructure.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:data/arquivo/dataset-arquivos.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ArquivoJpaRepositoryTest {

    @Autowired
    private ArquivoJpaRepository repository;

    @Test
    void testFindByNomeExists() {
        Optional<ArquivoJpa> opt = repository.findByNome("file1");
        assertTrue(opt.isPresent(), "Deve encontrar arquivo com nome file1");
        ArquivoJpa a = opt.get();
        assertEquals("id1", a.getId());
        assertEquals("file1", a.getNome());
        assertEquals("text/plain", a.getTipoDeConteudo());
        assertArrayEquals(new byte[]{0x01, 0x02}, a.getBytes());
        assertEquals("desc1", a.getDescricao());
    }

    @Test
    void testFindByNomeNotExists() {
        Optional<ArquivoJpa> opt = repository.findByNome("nonexistent");
        assertFalse(opt.isPresent(), "Não deve encontrar arquivo inexistente");
    }

    @Test
    void testChecaSeExisteArquivoComNome() {
        // Para o mesmo ID não considera duplicado
        assertFalse(repository.checaSeExisteArquivoComNome("file1", "id1"));
        // Para outro ID com mesmo nome retorna true
        assertTrue(repository.checaSeExisteArquivoComNome("file1", "id2"));
    }
}