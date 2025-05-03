package br.jus.trf1.sipe.usuario.web;

import br.jus.trf1.sipe.AbstractWebTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.startsWith;

class UsuarioControllerTest extends AbstractWebTest {

    @Test
    @Sql({"/data/usuario/usuario_test_existe.sql"})
    void buscaUsuarioComSucesso200() {
        var path = "http://localhost:" + RestAssured.port + "/v1/sipe/usuarios/1";

        given().
                expect().
                statusCode(200).
                body("id", equalTo(1)).
                body("nome", equalTo("RAFAEL BENZAQUEM NETO")).
                body("matricula", equalTo("RR20178")).
                body("cracha", equalTo("0000000000020178")).
                body("hora_diaria", equalTo(7)).
                body("_links.self.href", equalTo(path)).
                body("_links.delete.href", equalTo(path)).
                when().
                get(path);
    }

    @Test
    @Sql("/data/usuario/usuario_test_nao_existe.sql")
    void buscaUsuarioInexistente404() {
        given().
                expect().
                statusCode(404).
                when().
                get("/v1/sipe/usuarios/999");
    }

    @Test
    @Sql("/data/usuario/usuario_test_lista.sql")
    void listaUsuariosComSucesso200() {
        given().
                expect().
                statusCode(200).
                body("page.totalElements", equalTo(3)).
                when().
                get("/v1/sipe/usuarios?page=0&size=3");
    }

    @Test
    @Sql("/data/usuario/usuario_test_existe.sql")
    void apagaUsuarioComSucesso200() {
        given().
                expect().
                statusCode(200).
                body("id", equalTo(1)).
                body("nome", equalTo("RAFAEL BENZAQUEM NETO")).
                body("matricula", equalTo("RR20178")).
                body("cracha", equalTo("0000000000020178")).
                body("hora_diaria", equalTo(7)).
                when().
                delete("/v1/sipe/usuarios/1");
    }


    @Test
    @Sql("/data/usuario/usuario_test_existe.sql")
    void apagaUsuarioException404() {
        given().
                expect().
                statusCode(404).
                body("status_code", equalTo(404)).
                body("mensagem", equalTo("Não existe Usuario com id: 2")).
                body("timestamp", isA(Long.class)).
                body("path", equalTo("/v1/sipe/usuarios/2")).
                when().
                delete("/v1/sipe/usuarios/2");
    }

    // -- Testes de criação de usuário --
    @Test
    @Sql("/data/usuario/usuario_test_nao_existe.sql")
    void criaUsuarioSucesso201() {
        var payload = """
            {
              "nome": "Teste Nome",
              "matricula": "M123",
              "cracha": "C123",
              "hora_diaria": 5
            }
        """;
        given().
                contentType("application/json").
                body(payload).
        expect().
                statusCode(201).
                body("id", isA(Integer.class)).
                body("nome", equalTo("Teste Nome")).
                body("matricula", equalTo("M123")).
                body("cracha", equalTo("C123")).
                body("hora_diaria", equalTo(5)).
                body("_links.self.href", startsWith("http://localhost:" + RestAssured.port + "/v1/sipe/usuarios/" )).
                body("_links.delete.href", startsWith("http://localhost:" + RestAssured.port + "/v1/sipe/usuarios/" )).
        when().
                post("/v1/sipe/usuarios");
    }

    @Test
    @Sql("/data/usuario/usuario_test_existe.sql")
    void criaUsuarioFalhaMatriculaDuplicada() {
        var payload = """
            {
              "nome": "Usuario Teste",
              "matricula": "RR20178",
              "cracha": "NEW_CRACHA",
              "hora_diaria": 7
            }
        """;
        given().
                contentType("application/json").
                body(payload).
        expect().
                statusCode(400).
                body("status_code", equalTo(400)).
                body("mensagem", equalTo("Erro de validação.")).
                body("erros[0].parametro", equalTo("matricula")).
                body("erros[0].mensagem", equalTo("Esta matrícula já foi cadastrada no banco de dados!" )).
        when().
                post("/v1/sipe/usuarios");
    }

    @Test
    @Sql("/data/usuario/usuario_test_existe.sql")
    void criaUsuarioFalhaCrachaDuplicado() {
        var payload = """
            {
              "nome": "Usuario Teste",
              "matricula": "NEW_MATRICULA",
              "cracha": "0000000000020178",
              "hora_diaria": 7
            }
        """;
        given().
                contentType("application/json").
                body(payload).
        expect().
                statusCode(400).
                body("status_code", equalTo(400)).
                body("mensagem", equalTo("Erro de validação.")).
                body("erros[0].parametro", equalTo("cracha")).
                body("erros[0].mensagem", equalTo("Este crachá já foi cadastrado no banco de dados!" )).
        when().
                post("/v1/sipe/usuarios");
    }

    // -- Testes de atualização de usuário --
    @Test
    @Sql("/data/usuario/usuario_test_existe.sql")
    void atualizaUsuarioSucesso200() {
        var payload = """
            {
              "nome": "Nome Atualizado",
              "matricula": "RR20178",
              "cracha": "0000000000020178",
              "hora_diaria": 10
            }
        """;
        given().
                contentType("application/json").
                body(payload).
        expect().
                statusCode(200).
                body("id", equalTo(1)).
                body("nome", equalTo("Nome Atualizado")).
                body("hora_diaria", equalTo(10)).
        when().
                put("/v1/sipe/usuarios/1");
    }

    @Test
    @Sql("/data/usuario/usuario_test_existe.sql")
    void atualizaUsuarioFalhaValidacaoNomeVazio() {
        var payload = """
            {
              "nome": "",
              "matricula": "RR20178",
              "cracha": "0000000000020178",
              "hora_diaria": 7
            }
        """;
        given().
                contentType("application/json").
                body(payload).
        expect().
                statusCode(400).
                body("status_code", equalTo(400)).
                body("mensagem", equalTo("Erro de validação.")).
                body("erros[0].parametro", equalTo("nome")).
                body("erros[0].mensagem", equalTo("Campo 'nome' não pode ser vazio ou em branco." )).
        when().
                put("/v1/sipe/usuarios/1");
    }

    @Test
    @Sql("/data/usuario/usuario_test_nao_existe.sql")
    void atualizaUsuarioInexistente404() {
        var payload = """
            {
              "nome": "Novo Nome",
              "matricula": "M000",
              "cracha": "C000",
              "hora_diaria": 5
            }
        """;
        given().
                contentType("application/json").
                body(payload).
        expect().
                statusCode(404).
                body("status_code", equalTo(404)).
                body("mensagem", equalTo("Não existe Usuario com id: 1")).
                body("path", equalTo("/v1/sipe/usuarios/1")).
        when().
                put("/v1/sipe/usuarios/1");
    }

    @Test
    @Sql("/data/usuario/dataset-usuarios.sql")
    void atualizaUsuarioFalhaDuplicataMatricula() {
        var payload = """
            {
              "nome": "Bob Builder",
              "matricula": "M001",
              "cracha": "C002",
              "hora_diaria": 6
            }
        """;
        given().
                contentType("application/json").
                body(payload).
        expect().
                statusCode(400).
                body("status_code", equalTo(400)).
                body("mensagem", equalTo("Erro de validação.")).
                body("erros[0].parametro", equalTo("matricula")).
                body("erros[0].mensagem", equalTo("Existe usuário com matrícula = M001" )).
        when().
                put("/v1/sipe/usuarios/2");
    }

    @Test
    @Sql("/data/usuario/dataset-usuarios.sql")
    void atualizaUsuarioFalhaDuplicataCracha() {
        var payload = """
            {
              "nome": "Bob Builder",
              "matricula": "M002",
              "cracha": "C001",
              "hora_diaria": 6
            }
        """;
        given().
                contentType("application/json").
                body(payload).
        expect().
                statusCode(400).
                body("status_code", equalTo(400)).
                body("mensagem", equalTo("Erro de validação.")).
                body("erros[0].parametro", equalTo("cracha")).
                body("erros[0].mensagem", equalTo("Existe usuário com crachá = C001" )).
        when().
                put("/v1/sipe/usuarios/2");
    }
}