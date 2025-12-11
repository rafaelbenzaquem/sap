package br.jus.trf1.sipe.ponto.db;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.PontoId;
import br.jus.trf1.sipe.ponto.PontoRepository;
import br.jus.trf1.sipe.usuario.infrastructure.db.UsuarioJpa;
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
@Sql(scripts = "classpath:data/ponto/dataset-pontos.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PontoRepositoryTest {

    @Autowired
    private PontoRepository repository;

    @Test
    void testBuscaPontosPorPeriodo() {
        var inicio = LocalDate.of(2023, 1, 1);
        var fim = LocalDate.of(2023, 1, 2);
        List<Ponto> list = repository.buscaPontosPorPeriodo("M1", inicio, fim);
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(p -> p.getId().equals(PontoId.builder().usuarioJPA(UsuarioJpa.builder()
                .matricula("M1")
                .build()).dia(inicio).build())));
        assertTrue(list.stream().anyMatch(p -> p.getId().equals(PontoId.builder().usuarioJPA(UsuarioJpa.builder()
                .matricula("M1")
                .build()).dia(fim).build())));
    }

    @Test
    void testBuscaPontoExists() {
        var dia = LocalDate.of(2023, 1, 1);
        Optional<Ponto> opt = repository.buscaPonto("M1", dia);
        assertTrue(opt.isPresent());
        Ponto p = opt.get();
        assertEquals("M1", p.getId().getUsuarioJPA().getMatricula());
        assertEquals(dia, p.getId().getDia());
    }

    @Test
    void testBuscaPontoNotExists() {
        var dia = LocalDate.of(2023, 1, 3);
        Optional<Ponto> opt = repository.buscaPonto("M1", dia);
        assertFalse(opt.isPresent());
    }
}