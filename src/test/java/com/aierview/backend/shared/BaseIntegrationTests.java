package com.aierview.backend.shared;

import com.github.tomakehurst.wiremock.WireMockServer;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(properties = "spring.profiles.active=test", webEnvironment = RANDOM_PORT)
public class BaseIntegrationTests {
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
    private static WireMockServer wireMockServer;
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected EntityManager entityManager;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeAll
    static void startWireMockContainer() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();

        wireMockServer.stubFor(get(urlPathEqualTo("/tokeninfo"))
                .withQueryParam("id_token", equalTo("any_valid_token"))
                .willReturn(okJson("""
                            {
                              "email": "example@example.com",
                              "name": "John Snow Smith",
                              "picture": "any_picture"
                            }
                        """)));

        wireMockServer.stubFor(get(urlPathEqualTo("/tokeninfo"))
                .withQueryParam("id_token", equalTo("any_invalid_token"))
                .willReturn(aResponse().withStatus(400)));
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        databaseCleaner.clearDatabase();
    }
}
