package br.jus.trf1.sipe.ponto.infrastructure.jpa;

import br.jus.trf1.sipe.usuario.infrastructure.jpa.UsuarioJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:data/usuario/dataset-usuarios.sql", "classpath:data/ponto/dataset-pontos.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PontoJpaRepositoryTest {

    @Autowired
    private PontoJpaRepository pontoJpaRepository;

    @Test
    void testBuscaPontosPorPeriodo() {
        var inicio = LocalDate.of(2023, 1, 1);
        var fim = LocalDate.of(2023, 1, 2);
        List<PontoJpa> list = pontoJpaRepository.buscaPontosPorPeriodo("M001", inicio, fim);
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(p -> p.getId().equals(PontoJpaId.builder().usuario(UsuarioJpa.builder()
                .matricula("M001")
                .build()).dia(inicio).build())));
        assertTrue(list.stream().anyMatch(p -> p.getId().equals(PontoJpaId.builder().usuario(UsuarioJpa.builder()
                .matricula("M001")
                .build()).dia(fim).build())));
    }

    @Test
    void testBuscaPontoExists() {
        var dia = LocalDate.of(2023, 1, 1);
        var pontoJpaOpt = pontoJpaRepository.buscaPonto("M001", dia);
        assertTrue(pontoJpaOpt.isPresent());
        PontoJpa pontoJpa = pontoJpaOpt.get();

        assertEquals("M001", pontoJpa.getId().getUsuario().getMatricula());
        assertEquals(dia, pontoJpa.getId().getDia());
    }

}