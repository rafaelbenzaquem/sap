package br.jus.trf1.sipe.arquivo.web;

import br.jus.trf1.sipe.AbstractWebTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.startsWith;

class ArquivoControllerTest extends AbstractWebTest {

    @Test
    @Sql("/data/arquivo/arquivo_test_nao_existe.sql")
    void salvaArquivoSucesso201() {
        byte[] content = new byte[]{10, 20};
        given()
                .multiPart("bytes", "file.bin", content, "application/octet-stream")
                .param("nome", "file1")
                .param("descricao", "desc1")
        .expect()
                .statusCode(201)
                .body("id", isA(String.class))
                .body("nome", equalTo("file1"))
                .body("tipoDeConteudo", equalTo("application/octet-stream"))
                .body("tamanho", equalTo(16))
                .body("descricao", equalTo("desc1"))
                .body("_links.self.href", startsWith("http://localhost:" + RestAssured.port + "/v1/sipe/arquivos/"))
        .when()
                .post("/v1/sipe/arquivos");
    }
    // -- Teste de criação duplicada --
    @Test
    @Sql("/data/arquivo/arquivo_test_existe.sql")
    void salvaArquivoFalhaNomeDuplicado() {
        byte[] content = new byte[]{10, 20};
        given()
                .multiPart("bytes", "file.bin", content, "application/octet-stream")
                .param("nome", "file1")
                .param("descricao", "desc1")
        .expect()
                .statusCode(400)
                .body("status_code", equalTo(400))
                .body("mensagem", equalTo("Erro de validação."))
                .body("erros[0].parametro", equalTo("nome"))
                .body("erros[0].mensagem", equalTo("já existe arquivo com nome file1"))
        .when()
                .post("/v1/sipe/arquivos");
    }

    @Test
    @Sql("/data/arquivo/arquivo_test_existe.sql")
    void atualizaArquivoSucesso201() {
        byte[] content = new byte[]{30, 40, 50};
        given()
                .multiPart("bytes", "file.bin", content, "application/octet-stream")
                .param("nome", "file1up")
                .param("descricao", "descUp")
        .expect()
                .statusCode(201)
                .body("id", equalTo("1"))
                .body("nome", equalTo("file1up"))
                .body("descricao", equalTo("descUp"))
        .when()
                .patch("/v1/sipe/arquivos/1");
    }

    // -- Teste de atualização duplicada --
    @Test
    @Sql("/data/arquivo/dataset-arquivos.sql")
    void atualizaArquivoFalhaNomeDuplicado() {
        byte[] content = new byte[]{100};
        given()
                .multiPart("bytes", "file.bin", content, "application/octet-stream")
                .param("nome", "file1")
                .param("descricao", "descX")
        .expect()
                .statusCode(400)
                .body("status_code", equalTo(400))
                .body("mensagem", equalTo("Erro de validação."))
                .body("erros[0].parametro", equalTo("nome"))
                .body("erros[0].mensagem", equalTo("já existe arquivo com nome file1"))
        .when()
                .patch("/v1/sipe/arquivos/id2");
    }

    @Test
    @Sql("/data/arquivo/arquivo_test_lista.sql")
    void listaArquivosSucesso200() {
        given()
        .expect()
                .statusCode(200)
                .body("totalElements", equalTo(3))
        .when()
                .get("/v1/sipe/arquivos?pag=0&tamanho=3");
    }

    @Test
    @Sql("/data/arquivo/arquivo_test_existe.sql")
    void getConteudoPorIdSucesso200() {
        given()
        .expect()
                .statusCode(200)
                .header("Content-Type", startsWith("text/plain"))
        .when()
                .get("/v1/sipe/arquivos/1");
    }

    @Test
    @Sql("/data/arquivo/arquivo_test_existe.sql")
    void getMetadataPorIdSucesso200() {
        given()
        .expect()
                .statusCode(200)
                .body("id", equalTo("1"))
                .body("nome", equalTo("file1"))
                .body("tipoDeConteudo", equalTo("text/plain"))
                .body("tamanho", equalTo(16))
                .body("descricao", equalTo("desc1"))
        .when()
                .get("/v1/sipe/arquivos/1/metadata");
    }

    // -- Testes de recuperação por nome --
    @Test
    @Sql("/data/arquivo/arquivo_test_existe.sql")
    void getConteudoPorNomeSucesso200() {
        given()
        .expect()
                .statusCode(200)
                .header("Content-Type", startsWith("text/plain"))
        .when()
                .get("/v1/sipe/arquivos/nome/file1");
    }

    @Test
    @Sql("/data/arquivo/arquivo_test_existe.sql")
    void getMetadataPorNomeSucesso200() {
        given()
        .expect()
                .statusCode(200)
                .body("id", equalTo("1"))
                .body("nome", equalTo("file1"))
                .body("tipoDeConteudo", equalTo("text/plain"))
                .body("tamanho", equalTo(16))
                .body("descricao", equalTo("desc1"))
        .when()
                .get("/v1/sipe/arquivos/nome/file1/metadata");
    }

    // -- Testes de delete por id --
    @Test
    @Sql("/data/arquivo/arquivo_test_existe.sql")
    void deletePorIdSucesso200() {
        given()
        .expect()
                .statusCode(200)
                .body("id", equalTo("1"))
                .body("nome", equalTo("file1"))
        .when()
                .delete("/v1/sipe/arquivos/1");
    }

    @Test
    @Sql("/data/arquivo/arquivo_test_nao_existe.sql")
    void deletePorIdNaoEncontrado404() {
        given()
        .expect()
                .statusCode(404)
                .body("status_code", equalTo(404))
                .body("mensagem", equalTo("Não existe Arquivo com id: 1"))
                .body("path", equalTo("/v1/sipe/arquivos/1"))
        .when()
                .delete("/v1/sipe/arquivos/1");
    }
}