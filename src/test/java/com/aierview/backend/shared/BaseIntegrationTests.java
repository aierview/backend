package com.aierview.backend.shared;

import com.github.tomakehurst.wiremock.WireMockServer;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


@Testcontainers
public class BaseIntegrationTests {
    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.1"));

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
    private static WireMockServer wireMockServer;
    protected final EntityManager entityManager;
    protected final DatabaseCleaner databaseCleaner;

    public BaseIntegrationTests(EntityManager em, DatabaseCleaner dc) {
        this.entityManager = em;
        this.databaseCleaner = dc;
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @BeforeAll
    static void startWireMockContainer() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();

        //GOOGLE -  get token info
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

        // GEMINI - generate questions
        wireMockServer.stubFor(post(urlPathEqualTo("/gemini/generateContent"))
                .withQueryParam("key", equalTo("any_valid_api_key"))
                .willReturn(okJson(fakeResponse())));

        wireMockServer.stubFor(post(urlPathEqualTo("/gemini/generateContent"))
                .withQueryParam("key", equalTo("any_invalid_api_key"))
                .willReturn(aResponse().withStatus(400)));
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    private static String fakeResponse() {
        Map<String, Object> fakePart = new HashMap<>();
        fakePart.put("text", "Primeira linha ## Segunda linha");

        Map<String, Object> fakeContent = new HashMap<>();
        fakeContent.put("parts", List.of(fakePart));

        Map<String, Object> fakeCandidate = new HashMap<>();
        fakeCandidate.put("content", fakeContent);

        Map<String, Object> fakeResponse = new HashMap<>();
        fakeResponse.put("candidates", List.of(fakeCandidate));

        try {
            return new ObjectMapper().writeValueAsString(fakeResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @BeforeEach
    public void setup() {
        databaseCleaner.clearDatabase();
    }
}

