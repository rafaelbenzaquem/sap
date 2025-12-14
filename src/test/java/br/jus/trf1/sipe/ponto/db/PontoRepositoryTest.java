package br.jus.trf1.sipe.ponto.db;

import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.ponto.domain.model.PontoId;
import br.jus.trf1.sipe.ponto.domain.port.out.PontoPersistencePort;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
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
@Sql(scripts = "classpath:data/ponto/dataset-pontos.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PontoRepositoryTest {

    @Autowired
    private PontoPersistencePort repositoryPersistencePort;

    @Test
    void testBuscaPontosPorPeriodo() {
        var inicio = LocalDate.of(2023, 1, 1);
        var fim = LocalDate.of(2023, 1, 2);
        List<Ponto> list = repositoryPersistencePort.buscaPontosPorPeriodo("M1", inicio, fim);
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(p -> p.getId().equals(PontoId.builder().usuario(Usuario.builder()
                .matricula("M1")
                .build()).dia(inicio).build())));
        assertTrue(list.stream().anyMatch(p -> p.getId().equals(PontoId.builder().usuario(Usuario.builder()
                .matricula("M1")
                .build()).dia(fim).build())));
    }

    @Test
    void testBuscaPontoExists() {
        var dia = LocalDate.of(2023, 1, 1);
        Ponto ponto = repositoryPersistencePort.busca("M1", dia);
        assertEquals("M1", ponto.getId().getUsuario().getMatricula());
        assertEquals(dia, ponto.getId().getDia());
    }

}