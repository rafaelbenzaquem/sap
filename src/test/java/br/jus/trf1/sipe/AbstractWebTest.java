package br.jus.trf1.sipe;

import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractWebTest {

    @Value("${local.server.port}")
    private int port;

    @BeforeEach
    public void setup(){
        log.info("ServerTest executing in port {}", port);
        RestAssured.port = port;
    }
}
