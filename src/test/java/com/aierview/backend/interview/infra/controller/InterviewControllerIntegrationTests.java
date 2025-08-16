package com.aierview.backend.interview.infra.controller;

import com.aierview.backend.auth.domain.model.local.LocalSigninRequest;
import com.aierview.backend.auth.infra.persistence.entity.AuthJpaEntity;
import com.aierview.backend.auth.infra.persistence.entity.UserJpaEntity;
import com.aierview.backend.shared.DatabaseCleaner;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import com.aierview.backend.shared.testdata.HttpServletTestFixture;
import com.aierview.backend.shared.testdata.InterviewTestFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(properties = "spring.profiles.active=test", webEnvironment = RANDOM_PORT)
public class InterviewControllerIntegrationTests {
    static GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis:7.2.0-alpine"))
            .withExposedPorts(6379);

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.1"));

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
    private static WireMockServer wireMockServer;
    private final String LOCAL_SIGNIN_API_URL = "/api/v1/auth/local/signin";
    private final String BEGIN_INTERVIEW_API_URL = "/api/v1/interview/begin";


    @Autowired
    private MockMvc mvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
//      KAFKA CONFIG
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
//      REDIS CONFIG
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
    }

    @BeforeAll
    static void startWireMockContainer() {
        redisContainer.start();
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
                .willReturn(okJson(InterviewTestFixture.fakeResponse())));

        wireMockServer.stubFor(post(urlPathEqualTo("/gemini/generateContent"))
                .withQueryParam("key", equalTo("any_invalid_api_key"))
                .willReturn(aResponse().withStatus(400)));
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
        postgresContainer.close();
    }

    @BeforeEach
    public void setup() {
        databaseCleaner.clearDatabase();
    }

    @Transactional
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should return 400 when stack is null or empty")
    void shouldReturn400WhenStackIsNullOrEmpty(String stack) throws Exception {
        UserJpaEntity userJpaEntity = AuthTestFixture.anyUserJpaEntity();
        entityManager.persist(userJpaEntity);
        entityManager.flush();

        String password = "any_password";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        AuthJpaEntity authJpaEntity = AuthTestFixture.anyAuthJpaEntity(userJpaEntity, hashedPassword);
        entityManager.persist(authJpaEntity);
        entityManager.flush();

        LocalSigninRequest requestBody = new LocalSigninRequest(userJpaEntity.getEmail(), password);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNIN_API_URL, json);

        var result = mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("data",
                        Matchers.is("OK")))
                .andExpect(cookie().exists("token"))
                .andExpect(cookie().value("token", Matchers.notNullValue()))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, Matchers.containsString("token=")))
                .andReturn();


        //Making request with headers
        var beginInterviewRequestBody = InterviewTestFixture.anyBeginInterviewRequest();
        beginInterviewRequestBody.setStack(stack);
        json = new ObjectMapper().writeValueAsString(beginInterviewRequestBody);
        List<Cookie> cookies = List.of(result.getResponse().getCookies());
        request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.BEGIN_INTERVIEW_API_URL, json);
        request.cookie(cookies.getFirst()); //

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Stack is required!")));
    }

    @Test
    @Transactional
    @DisplayName("Should return 202 begin interview succeeds")
    void shouldReturn202WhenBeginInterviewSucceeds() throws Exception {
        UserJpaEntity userJpaEntity = AuthTestFixture.anyUserJpaEntity();
        entityManager.persist(userJpaEntity);
        entityManager.flush();

        String password = "any_password";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        AuthJpaEntity authJpaEntity = AuthTestFixture.anyAuthJpaEntity(userJpaEntity, hashedPassword);
        entityManager.persist(authJpaEntity);
        entityManager.flush();

        LocalSigninRequest requestBody = new LocalSigninRequest(userJpaEntity.getEmail(), password);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNIN_API_URL, json);

        var result = mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("data",
                        Matchers.is("OK")))
                .andExpect(cookie().exists("token"))
                .andExpect(cookie().value("token", Matchers.notNullValue()))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, Matchers.containsString("token=")))
                .andReturn();

        //Making request with headers
        var beginInterviewRequestBody = InterviewTestFixture.anyBeginInterviewRequest();
        json = new ObjectMapper().writeValueAsString(beginInterviewRequestBody);
        List<Cookie> cookies = List.of(result.getResponse().getCookies());
        request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.BEGIN_INTERVIEW_API_URL, json);
        request.cookie(cookies.getFirst()); //

        mvc
                .perform(request)
                .andExpect(status().isAccepted());
    }
}
