package br.jus.trf1.sipe.arquivo;

import br.jus.trf1.sipe.arquivo.db.ArquivoRepository;
import br.jus.trf1.sipe.arquivo.exceptions.ArquivoInexistenteException;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoAtualizadoRequest;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoListResponse;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoNovoRequest;
import br.jus.trf1.sipe.comum.exceptions.CamposUnicosExistentesException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:data/arquivo/dataset-arquivos.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ArquivoServiceIntegrationTest {

    @Autowired
    private ArquivoService service;

    @Autowired
    private ArquivoRepository repository;

    @Test
    void testArmazenaSucesso() {
        var request = ArquivoNovoRequest.builder()
                .id("id3")
                .nome("file3")
                .tipoDeConteudo("text/plain")
                .bytes(new byte[]{3, 4})
                .descricao("desc3")
                .build();
        var meta = service.armazena(request);
        assertNotNull(meta);
        assertEquals("id3", meta.id());
        assertEquals("file3", meta.nome());
        assertEquals("text/plain", meta.tipoDeConteudo());
        assertEquals(16L, meta.tamanho());
        assertEquals("desc3", meta.descricao());
    }

    @Test
    void testArmazenaNomeDuplicado() {
        var request = ArquivoNovoRequest.builder()
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
        Page<ArquivoListResponse> page = service.lista(0, 10);
        assertEquals(2, page.getTotalElements());
    }

    @Test
    void testRecuperaPorIdENome() {
        var resp1 = service.recuperaPorId("id1");
        assertEquals("file1", resp1.nome());
        var resp2 = service.recuperaPorNome("file2");
        assertEquals("file2", resp2.nome());
    }

    @Test
    void testRecuperaPorIdInexistente() {
        assertThrows(ArquivoInexistenteException.class, () -> service.recuperaPorId("no"));
    }

    @Test
    void testRecuperaMetadata() {
        var meta1 = service.recuperaMetadataPorId("id1");
        assertEquals("file1", meta1.nome());
        var meta2 = service.recuperaMetadataPorNome("file2");
        assertEquals("file2", meta2.nome());
    }

    @Test
    void testAtualizaSucesso() {
        var request = ArquivoAtualizadoRequest.builder()
                .id("id1")
                .nome("file1new")
                .tipoDeConteudo("text/plain")
                .bytes(new byte[]{7, 8, 9})
                .descricao("descNew")
                .build();
        var meta = service.atualiza(request);
        assertEquals("file1new", meta.nome());
        assertEquals(24L, meta.tamanho());
    }

    @Test
    void testAtualizaInexistente() {
        var request = ArquivoAtualizadoRequest.builder()
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
        var request = ArquivoAtualizadoRequest.builder()
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
        assertEquals("file1", meta.nome());
        assertEquals(1, repository.count());
        assertThrows(ArquivoInexistenteException.class, () -> service.apagaPorId("no"));
    }

    @Test
    void testApagaPorNome() {
        var meta = service.apagaPorNome("file2");
        assertEquals("file2", meta.nome());
        assertEquals(1, repository.count());
    }
}