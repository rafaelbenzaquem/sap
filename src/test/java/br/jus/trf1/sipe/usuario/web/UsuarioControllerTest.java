package br.jus.trf1.sipe.usuario.web;

import br.jus.trf1.sipe.AbstractWebTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.isA;

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

}