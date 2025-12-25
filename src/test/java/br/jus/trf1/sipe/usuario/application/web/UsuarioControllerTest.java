package br.jus.trf1.sipe.usuario.application.web;

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

@DisplayName("Testes do UsuarioController")
class UsuarioControllerTest extends AbstractWebTest {

    @Nested
    @DisplayName("GET /v1/sipe/usuarios/{matricula}")
    class BuscaUsuarioPorId {

        @Test
        @Sql({"/data/usuario/usuario_test_existe.sql"})
        @DisplayName("Deve retornar usuário com sucesso quando autenticado")
        void buscaUsuarioComSucesso200() {
            var path = "http://localhost:" + RestAssured.port + "/v1/sipe/usuarios/RR20178";

            given()
                    .when()
                    .get(path)
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(1))
                    .body("nome", equalTo("RAFAEL BENZAQUEM NETO"))
                    .body("matricula", equalTo("RR20178"))
                    .body("cracha", equalTo(20178))
                    .body("hora_diaria", equalTo(7))
                    .body("_links.self.href", equalTo(path))
                    .body("_links.delete.href", equalTo(path));
        }

        @Test
        @Sql("/data/usuario/usuario_test_nao_existe.sql")
        @DisplayName("Deve retornar 404 quando usuário não existe")
        void buscaUsuarioInexistente404() {
            givenAuth()
                    .when()
                    .get("/v1/sipe/usuarios/999")
                    .then()
                    .statusCode(404);
        }

        @Test
        @Sql("/data/usuario/usuario_test_existe.sql")
        @DisplayName("Deve retornar 401 quando não autenticado")
        void buscaUsuarioSemAutenticacao401() {
            given()
                    .when()
                    .get("/v1/sipe/usuarios/1")
                    .then()
                    .statusCode(401);
        }
    }

    @Nested
    @DisplayName("GET /v1/sipe/usuarios")
    class ListaUsuarios {

        @Test
        @Sql("/data/usuario/usuario_test_lista.sql")
        @DisplayName("Deve listar usuários com sucesso quando autenticado")
        void listaUsuariosComSucesso200() {
            givenAuth()
                    .when()
                    .get("/v1/sipe/usuarios?page=0&size=3")
                    .then()
                    .statusCode(200)
                    .body("page.totalElements", equalTo(3));
        }

        @Test
        @Sql("/data/usuario/usuario_test_lista.sql")
        @DisplayName("Deve retornar 401 quando não autenticado")
        void listaUsuariosSemAutenticacao401() {
            given()
                    .when()
                    .get("/v1/sipe/usuarios?page=0&size=3")
                    .then()
                    .statusCode(401);
        }
    }

    @Nested
    @DisplayName("DELETE /v1/sipe/usuarios/{id}")
    class ApagaUsuario {

        @Test
        @Sql("/data/usuario/usuario_test_existe.sql")
        @DisplayName("Deve apagar usuário com sucesso quando autenticado como admin")
        void apagaUsuarioComSucesso200() {
            givenAuth()
                    .when()
                    .delete("/v1/sipe/usuarios/1")
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(1))
                    .body("nome", equalTo("RAFAEL BENZAQUEM NETO"))
                    .body("matricula", equalTo("RR20178"))
                    .body("cracha", equalTo("0000000000020178"))
                    .body("hora_diaria", equalTo(7));
        }

        @Test
        @Sql("/data/usuario/usuario_test_existe.sql")
        @DisplayName("Deve retornar 404 quando usuário a ser apagado não existe")
        void apagaUsuarioException404() {
            givenAuth()
                    .when()
                    .delete("/v1/sipe/usuarios/2")
                    .then()
                    .statusCode(404)
                    .body("status_code", equalTo(404))
                    .body("mensagem", equalTo("Não existe UsuarioJpa com id: 2"))
                    .body("timestamp", isA(Long.class))
                    .body("path", equalTo("/v1/sipe/usuarios/2"));
        }

        @Test
        @Sql("/data/usuario/usuario_test_existe.sql")
        @DisplayName("Deve retornar 401 quando não autenticado")
        void apagaUsuarioSemAutenticacao401() {
            given()
                    .when()
                    .delete("/v1/sipe/usuarios/1")
                    .then()
                    .statusCode(401);
        }
    }

    @Nested
    @DisplayName("POST /v1/sipe/usuarios")
    class CriaUsuario {

        @Test
        @Sql("/data/usuario/usuario_test_nao_existe.sql")
        @DisplayName("Deve criar usuário com sucesso quando autenticado")
        void criaUsuarioSucesso201() {
            var payload = """
                {
                  "nome": "Teste Nome",
                  "matricula": "M123",
                  "cracha": "C123",
                  "hora_diaria": 5
                }
            """;

            givenAuth()
                    .body(payload)
                    .when()
                    .post("/v1/sipe/usuarios")
                    .then()
                    .statusCode(201)
                    .body("id", isA(Integer.class))
                    .body("nome", equalTo("Teste Nome"))
                    .body("matricula", equalTo("M123"))
                    .body("cracha", equalTo("C123"))
                    .body("hora_diaria", equalTo(5))
                    .body("_links.self.href", startsWith("http://localhost:" + RestAssured.port + "/v1/sipe/usuarios/"))
                    .body("_links.delete.href", startsWith("http://localhost:" + RestAssured.port + "/v1/sipe/usuarios/"));
        }

        @Test
        @Sql("/data/usuario/usuario_test_existe.sql")
        @DisplayName("Deve retornar 400 quando matrícula já existe")
        void criaUsuarioFalhaMatriculaDuplicada() {
            var payload = """
                {
                  "nome": "UsuarioJpa Teste",
                  "matricula": "RR20178",
                  "cracha": "NEW_CRACHA",
                  "hora_diaria": 7
                }
            """;

            givenAuth()
                    .body(payload)
                    .when()
                    .post("/v1/sipe/usuarios")
                    .then()
                    .statusCode(400)
                    .body("status_code", equalTo(400))
                    .body("mensagem", equalTo("Erro de validação."))
                    .body("erros[0].parametro", equalTo("matricula"))
                    .body("erros[0].mensagem", equalTo("Esta matrícula já foi cadastrada no banco de dados!"));
        }

        @Test
        @Sql("/data/usuario/usuario_test_existe.sql")
        @DisplayName("Deve retornar 400 quando crachá já existe")
        void criaUsuarioFalhaCrachaDuplicado() {
            var payload = """
                {
                  "nome": "UsuarioJpa Teste",
                  "matricula": "NEW_MATRICULA",
                  "cracha": "0000000000020178",
                  "hora_diaria": 7
                }
            """;

            givenAuth()
                    .body(payload)
                    .when()
                    .post("/v1/sipe/usuarios")
                    .then()
                    .statusCode(400)
                    .body("status_code", equalTo(400))
                    .body("mensagem", equalTo("Erro de validação."))
                    .body("erros[0].parametro", equalTo("cracha"))
                    .body("erros[0].mensagem", equalTo("Este crachá já foi cadastrado no banco de dados!"));
        }

        @Test
        @Sql("/data/usuario/usuario_test_nao_existe.sql")
        @DisplayName("Deve retornar 401 quando não autenticado")
        void criaUsuarioSemAutenticacao401() {
            var payload = """
                {
                  "nome": "Teste Nome",
                  "matricula": "M123",
                  "cracha": "C123",
                  "hora_diaria": 5
                }
            """;

            given()
                    .contentType("application/json")
                    .body(payload)
                    .when()
                    .post("/v1/sipe/usuarios")
                    .then()
                    .statusCode(401);
        }
    }

    @Nested
    @DisplayName("PUT /v1/sipe/usuarios/{matricula}")
    class AtualizaUsuario {

        @Test
        @Sql("/data/usuario/usuario_test_existe.sql")
        @DisplayName("Deve atualizar usuário com sucesso quando autenticado")
        void atualizaUsuarioSucesso200() {
            var payload = """
                {
                  "nome": "Nome Atualizado",
                  "matricula": "RR20178",
                  "cracha": 20178,
                  "hora_diaria": 10
                }
            """;

            givenAuth()
                    .body(payload)
                    .when()
                    .put("/v1/sipe/usuarios/RR20178")
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(1))
                    .body("nome", equalTo("Nome Atualizado"))
                    .body("hora_diaria", equalTo(10));
        }

        @Test
        @Sql("/data/usuario/usuario_test_existe.sql")
        @DisplayName("Deve retornar 400 quando nome está vazio")
        void atualizaUsuarioFalhaValidacaoNomeVazio() {
            var payload = """
                {
                  "nome": "",
                  "matricula": "RR20178",
                  "cracha": 20178,
                  "hora_diaria": 7
                }
            """;

            givenAuth()
                    .body(payload)
                    .when()
                    .put("/v1/sipe/usuarios/RR20178")
                    .then()
                    .statusCode(400)
                    .body("status_code", equalTo(400))
                    .body("mensagem", equalTo("Erro de validação."))
                    .body("erros[0].parametro", equalTo("nome"))
                    .body("erros[0].mensagem", equalTo("Campo 'nome' não pode ser vazio ou em branco."));
        }

        @Test
        @Sql("/data/usuario/usuario_test_nao_existe.sql")
        @DisplayName("Deve retornar 404 quando usuário não existe")
        void atualizaUsuarioInexistente404() {
            var payload = """
                {
                  "nome": "Novo Nome",
                  "matricula": "M000",
                  "cracha": "C000",
                  "hora_diaria": 5
                }
            """;

            givenAuth()
                    .body(payload)
                    .when()
                    .put("/v1/sipe/usuarios/1")
                    .then()
                    .statusCode(404)
                    .body("status_code", equalTo(404))
                    .body("mensagem", equalTo("Não existe UsuarioJpa com id: 1"))
                    .body("path", equalTo("/v1/sipe/usuarios/1"));
        }

        @Test
        @Sql("/data/usuario/dataset-usuarios.sql")
        @DisplayName("Deve retornar 400 quando matrícula já existe em outro usuário")
        void atualizaUsuarioFalhaDuplicataMatricula() {
            var payload = """
                {
                  "nome": "Bob Builder",
                  "matricula": "M001",
                  "cracha": "C002",
                  "hora_diaria": 6
                }
            """;

            givenAuth()
                    .body(payload)
                    .when()
                    .put("/v1/sipe/usuarios/2")
                    .then()
                    .statusCode(400)
                    .body("status_code", equalTo(400))
                    .body("mensagem", equalTo("Erro de validação."))
                    .body("erros[0].parametro", equalTo("matricula"))
                    .body("erros[0].mensagem", equalTo("Existe usuário com matrícula = M001"));
        }

        @Test
        @Sql("/data/usuario/dataset-usuarios.sql")
        @DisplayName("Deve retornar 400 quando crachá já existe em outro usuário")
        void atualizaUsuarioFalhaDuplicataCracha() {
            var payload = """
                {
                  "nome": "Bob Builder",
                  "matricula": "M002",
                  "cracha": "C001",
                  "hora_diaria": 6
                }
            """;

            givenAuth()
                    .body(payload)
                    .when()
                    .put("/v1/sipe/usuarios/2")
                    .then()
                    .statusCode(400)
                    .body("status_code", equalTo(400))
                    .body("mensagem", equalTo("Erro de validação."))
                    .body("erros[0].parametro", equalTo("cracha"))
                    .body("erros[0].mensagem", equalTo("Existe usuário com crachá = C001"));
        }

        @Test
        @Sql("/data/usuario/usuario_test_existe.sql")
        @DisplayName("Deve retornar 401 quando não autenticado")
        void atualizaUsuarioSemAutenticacao401() {
            var payload = """
                {
                  "nome": "Nome Atualizado",
                  "matricula": "RR20178",
                  "cracha": "0000000000020178",
                  "hora_diaria": 10
                }
            """;

            given()
                    .contentType("application/json")
                    .body(payload)
                    .when()
                    .put("/v1/sipe/usuarios/1")
                    .then()
                    .statusCode(401);
        }
    }
}