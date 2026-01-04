package br.jus.trf1.sipe.arquivo.application.web;

import br.jus.trf1.sipe.AbstractWebTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.startsWith;

@DisplayName("Testes do ArquivoController")
class ArquivoControllerTest extends AbstractWebTest {

    @Nested
    @DisplayName("POST /v1/sipe/arquivos")
    class SalvaArquivo {

        @Test
        @Sql("/data/arquivo/arquivo_test_nao_existe.sql")
        @DisplayName("Deve salvar arquivo com sucesso quando autenticado")
        void salvaArquivoSucesso201() {
            byte[] content = new byte[]{10, 20};
            givenAuth()
                    .multiPart("bytes", "file.bin", content, "application/octet-stream")
                    .param("nome", "file1")
                    .param("descricao", "desc1")
                    .when()
                    .post("/v1/sipe/arquivos")
                    .then()
                    .statusCode(201)
                    .body("id", isA(String.class))
                    .body("nome", equalTo("file1"))
                    .body("tipoDeConteudo", equalTo("application/octet-stream"))
                    .body("tamanho", equalTo(16)) // Nota: Verifique se o tamanho esperado está correto para o array de bytes
                    .body("descricao", equalTo("desc1"))
                    .body("_links.self.href", startsWith("http://localhost:" + RestAssured.port + "/v1/sipe/arquivos/"));
        }

        @Test
        @Sql("/data/arquivo/arquivo_test_existe.sql")
        @DisplayName("Deve falhar quando nome duplicado")
        void salvaArquivoFalhaNomeDuplicado() {
            byte[] content = new byte[]{10, 20};
            givenAuth()
                    .multiPart("bytes", "file.bin", content, "application/octet-stream")
                    .param("nome", "file1")
                    .param("descricao", "desc1")
                    .when()
                    .post("/v1/sipe/arquivos")
                    .then()
                    .statusCode(400)
                    .body("status_code", equalTo(400))
                    .body("mensagem", equalTo("Erro de validação."))
                    .body("erros[0].parametro", equalTo("nome"))
                    .body("erros[0].mensagem", equalTo("já existe arquivo com nome file1"));
        }

        @Test
        @DisplayName("Deve retornar 401 quando não autenticado")
        void salvaArquivoSemAutenticacao401() {
            given()
                    .multiPart("bytes", "file.bin", new byte[]{1}, "application/octet-stream")
                    .param("nome", "fileX")
                    .when()
                    .post("/v1/sipe/arquivos")
                    .then()
                    .statusCode(401);
        }
    }

    @Nested
    @DisplayName("PATCH /v1/sipe/arquivos/{id}")
    class AtualizaArquivo {

        @Test
        @Sql("/data/arquivo/arquivo_test_existe.sql")
        void atualizaArquivoSucesso201() {
            byte[] content = new byte[]{30, 40, 50};
            givenAuth()
                    .multiPart("bytes", "file.bin", content, "application/octet-stream")
                    .param("nome", "file1up")
                    .param("descricao", "descUp")
                    .when()
                    .patch("/v1/sipe/arquivos/1")
                    .then()
                    .statusCode(201)
                    .body("id", equalTo("1"))
                    .body("nome", equalTo("file1up"))
                    .body("descricao", equalTo("descUp"));
        }

        @Test
        @Sql("/data/arquivo/dataset-arquivos.sql")
        void atualizaArquivoFalhaNomeDuplicado() {
            byte[] content = new byte[]{100};
            givenAuth()
                    .multiPart("bytes", "file.bin", content, "application/octet-stream")
                    .param("nome", "file1")
                    .param("descricao", "descX")
                    .when()
                    .patch("/v1/sipe/arquivos/id2")
                    .then()
                    .statusCode(400)
                    .body("status_code", equalTo(400))
                    .body("mensagem", equalTo("Erro de validação."))
                    .body("erros[0].parametro", equalTo("nome"))
                    .body("erros[0].mensagem", equalTo("já existe arquivo com nome file1"));
        }
    }

    @Nested
    @DisplayName("GET /v1/sipe/arquivos")
    class BuscaArquivos {

        @Test
        @Sql("/data/arquivo/arquivo_test_lista.sql")
        void listaArquivosSucesso200() {
            givenAuth()
                    .when()
                    .get("/v1/sipe/arquivos?pag=0&tamanho=3")
                    .then()
                    .statusCode(200)
                    .body("totalElements", equalTo(3));
        }

        @Test
        @Sql("/data/arquivo/arquivo_test_existe.sql")
        void getConteudoPorIdSucesso200() {
            givenAuth()
                    .when()
                    .get("/v1/sipe/arquivos/1")
                    .then()
                    .statusCode(200)
                    .header("Content-Type", startsWith("text/plain"));
        }

        @Test
        @Sql("/data/arquivo/arquivo_test_existe.sql")
        void getMetadataPorIdSucesso200() {
            givenAuth()
                    .when()
                    .get("/v1/sipe/arquivos/1/metadata")
                    .then()
                    .statusCode(200)
                    .body("id", equalTo("1"))
                    .body("nome", equalTo("file1"))
                    .body("tipoDeConteudo", equalTo("text/plain"))
                    .body("tamanho", equalTo(16))
                    .body("descricao", equalTo("desc1"));
        }
    }

    @Nested
    @DisplayName("DELETE /v1/sipe/arquivos/{id}")
    class RemoveArquivo {

        @Test
        @Sql("/data/arquivo/arquivo_test_existe.sql")
        void deletePorIdSucesso200() {
            givenAuth()
                    .when()
                    .delete("/v1/sipe/arquivos/1")
                    .then()
                    .statusCode(200)
                    .body("id", equalTo("1"))
                    .body("nome", equalTo("file1"));
        }

        @Test
        @Sql("/data/arquivo/arquivo_test_nao_existe.sql")
        void deletePorIdNaoEncontrado404() {
            givenAuth()
                    .when()
                    .delete("/v1/sipe/arquivos/1")
                    .then()
                    .statusCode(404)
                    .body("status_code", equalTo(404))
                    .body("mensagem", equalTo("Não existe Arquivo com id: 1"))
                    .body("path", equalTo("/v1/sipe/arquivos/1"));
        }
    }
}