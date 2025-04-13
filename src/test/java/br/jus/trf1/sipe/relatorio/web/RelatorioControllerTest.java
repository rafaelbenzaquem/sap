package br.jus.trf1.sipe.relatorio.web;

import br.jus.trf1.sipe.AbstractWebTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

class RelatorioControllerTest extends AbstractWebTest {

    @Test
    @Sql({"/data/usuario/usuario_test_existe.sql",
            "/data/lotacao/lotacao_test_existe.sql",
            "/data/funcionario/funcionario_test_existe.sql"})
    void downloadRelatorio() {
        var path = "http://localhost:" + RestAssured.port + "/v1/sap/relatorios/20178?inicio=01082024&fim=31082024";

        given().
                expect().
                statusCode(200).
                body("nome", equalTo("RAFAEL BENZAQUEM NETO")).
                body("email", equalTo("rr20178@trf1.jus.br")).
                body("sigla_funcao", equalTo("SERSUT")).
                body("descricao_funcao", equalTo("SERVIÇO DE SUPORTE TÉCNICO AOS USUÁRIOS")).
                body("_links.self.href", equalTo(path)).
                body("_links.ferias.href", equalTo(path + "/ausencias/ferias")).
                body("_links.licencas.href", equalTo(path + "/ausencias/licencas")).
                body("_links.especiais.href", equalTo(path + "/ausencias/especiais")).
                when().
                get("/v1/sarh/servidores/RR20178");
    }

}