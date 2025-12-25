package br.jus.trf1.sipe;

import br.jus.trf1.sipe.comum.config.TestJwtDecoderConfig;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Classe base para testes funcionais de Controllers.
 * Configura RestAssured com porta aleatória e autenticação JWT de teste.
 *
 * <p>A configuração de segurança {@link TestJwtDecoderConfig} é detectada automaticamente
 * pelo Spring Boot no perfil "test", fornecendo um JwtDecoder que aceita tokens no formato:
 * "matricula|authority1,authority2,..."</p>
 *
 * <p>Uso:</p>
 * <pre>{@code
 * // Requisição autenticada com usuário padrão (ADMIN)
 * givenAuth()
 *     .get("/v1/sipe/usuarios/1")
 *     .then()
 *     .statusCode(200);
 *
 * // Requisição autenticada com usuário específico
 * givenAuthAs("M002", "GRP_SIPE_USERS")
 *     .get("/v1/sipe/usuarios/1")
 *     .then()
 *     .statusCode(200);
 *
 * // Requisição sem autenticação (deve retornar 401)
 * given()
 *     .get("/v1/sipe/usuarios/1")
 *     .then()
 *     .statusCode(401);
 * }</pre>
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractWebTest {

    protected static final String MATRICULA_PADRAO = "RR20178";
    protected static final String AUTHORITY_ADMIN = "GRP_SIPE_ADMIN";
    protected static final String AUTHORITY_DIRETOR = "GRP_SIPE_DIRETOR";
    protected static final String AUTHORITY_RH = "GRP_SIPE_RH";
    protected static final String AUTHORITY_USERS = "GRP_SIPE_USERS";

    @Value("${local.server.port}")
    private int port;

    @BeforeEach
    public void setup() {
        log.info("ServerTest executing in port {}", port);
        RestAssured.port = port;
    }

    /**
     * Retorna uma RequestSpecification autenticada com usuário admin padrão.
     * Usar para a maioria dos testes que precisam de autenticação.
     *
     * @return RequestSpecification com token JWT de admin
     */
    protected RequestSpecification givenAuth() {
        return givenAuthAs(MATRICULA_PADRAO, AUTHORITY_ADMIN, AUTHORITY_USERS);
    }

    /**
     * Retorna uma RequestSpecification autenticada com matrícula e authorities específicas.
     *
     * @param matricula   Matrícula do usuário
     * @param authorities Authorities (roles) do usuário
     * @return RequestSpecification com token JWT configurado
     */
    protected RequestSpecification givenAuthAs(String matricula, String... authorities) {
        String token = TestJwtDecoderConfig.criarTokenTeste(matricula, authorities);
        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json");
    }

    /**
     * Retorna uma RequestSpecification autenticada como usuário comum (apenas GRP_SIPE_USERS).
     *
     * @param matricula Matrícula do usuário
     * @return RequestSpecification com token JWT de usuário comum
     */
    protected RequestSpecification givenAuthAsUser(String matricula) {
        return givenAuthAs(matricula, AUTHORITY_USERS);
    }

    /**
     * Retorna uma RequestSpecification autenticada como diretor.
     *
     * @param matricula Matrícula do usuário
     * @return RequestSpecification com token JWT de diretor
     */
    protected RequestSpecification givenAuthAsDiretor(String matricula) {
        return givenAuthAs(matricula, AUTHORITY_DIRETOR, AUTHORITY_USERS);
    }

    /**
     * Retorna uma RequestSpecification autenticada como RH.
     *
     * @param matricula Matrícula do usuário
     * @return RequestSpecification com token JWT de RH
     */
    protected RequestSpecification givenAuthAsRH(String matricula) {
        return givenAuthAs(matricula, AUTHORITY_RH, AUTHORITY_USERS);
    }
}
