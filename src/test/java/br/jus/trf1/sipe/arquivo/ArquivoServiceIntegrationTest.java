package br.jus.trf1.sipe.arquivo;

import br.jus.trf1.sipe.arquivo.application.web.dto.ArquivoListResponse;
import br.jus.trf1.sipe.arquivo.domain.model.Arquivo;
import br.jus.trf1.sipe.arquivo.domain.port.out.ArquivoRepositoryPort;
import br.jus.trf1.sipe.arquivo.domain.service.ArquivoServiceDapter;
import br.jus.trf1.sipe.arquivo.exceptions.ArquivoInexistenteException;
import br.jus.trf1.sipe.comum.exceptions.CamposUnicosExistentesException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:data/arquivo/dataset-arquivos.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ArquivoServiceIntegrationTest {

    @Autowired
    private ArquivoServiceDapter service;

    @Autowired
    private ArquivoRepositoryPort repository;

    @Test
    void testArmazenaSucesso() {
        var request = Arquivo.builder()
                .id("id3")
                .nome("file3")
                .tipoDeConteudo("text/plain")
                .bytes(new byte[]{3, 4})
                .descricao("desc3")
                .build();
        var meta = service.armazena(request);
        assertNotNull(meta);
        assertEquals("id3", meta.getId());
        assertEquals("file3", meta.getNome());
        assertEquals("text/plain", meta.getTipoDeConteudo());
        assertEquals(16L, meta.getBytes());
        assertEquals("desc3", meta.getNome());
    }

    @Test
    void testArmazenaNomeDuplicado() {
        var request = Arquivo.builder()
                .id("id3")
                .nome("file1")
                .tipoDeConteudo("text/plain")
                .bytes(new byte[]{5})
                .descricao("descX")
                .build();
        assertThrows(CamposUnicosExistentesException.class, () -> service.armazena(request));
    }

    @Test
    void testLista() {
        List<Arquivo> arquivos = service.lista(0, 10);
        assertEquals(2, arquivos.size());
    }

    @Test
    void testRecuperaPorIdENome() {
        var resp1 = service.recuperaPorId("id1");
        assertEquals("file1", resp1.getNome());
        var resp2 = service.recuperaPorNome("file2");
        assertEquals("file2", resp2.getNome());
    }

    @Test
    void testRecuperaPorIdInexistente() {
        assertThrows(ArquivoInexistenteException.class, () -> service.recuperaPorId("no"));
    }



    @Test
    void testAtualizaSucesso() {
        var request = Arquivo.builder()
                .id("id1")
                .nome("file1new")
                .tipoDeConteudo("text/plain")
                .bytes(new byte[]{7, 8, 9})
                .descricao("descNew")
                .build();
        var meta = service.atualiza(request);
        assertEquals("file1new", meta.getNome());
        assertEquals(24L, meta.getBytes());
    }

    @Test
    void testAtualizaInexistente() {
        var request = Arquivo.builder()
                .id("no")
                .nome("x")
                .tipoDeConteudo("t")
                .bytes(new byte[]{1})
                .descricao(null)
                .build();
        assertThrows(ArquivoInexistenteException.class, () -> service.atualiza(request));
    }

    @Test
    void testAtualizaNomeDuplicado() {
        var request = Arquivo.builder()
                .id("id2")
                .nome("file1")
                .tipoDeConteudo("text/plain")
                .bytes(new byte[]{1})
                .descricao(null)
                .build();
        assertThrows(CamposUnicosExistentesException.class, () -> service.atualiza(request));
    }

    @Test
    void testApagaEInexistente() {
        var meta = service.apagaPorId("id1");
        assertEquals("file1", meta.getNome());
        assertEquals(1, repository.contar());
        assertThrows(ArquivoInexistenteException.class, () -> service.apagaPorId("no"));
    }

    @Test
    void testApagaPorNome() {
        var meta = service.apagaPorNome("file2");
        assertEquals("file2", meta.getNome());
        assertEquals(1, repository.contar());
    }
}