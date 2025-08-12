package com.aierview.backend.auth;

import com.aierview.backend.shared.DatabaseCleaner;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import com.aierview.backend.shared.testdata.HttpServletTestFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test-db-error")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthTestDatabaseError {
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private final String URL = "/api/v1/auth";
    private final String LOCAL_SIGNUP_API_URL = "/api/v1/auth/local/signup";
    private final String LOCAL_SIGNIN_API_URL = "/api/v1/auth/local/signin";
    private final String GOOGLE_SIGNUP_API_URL = "/api/v1/auth/google/signup";
    private final String GOOGLE_SIGNIN_API_URL = "/api/v1/auth/google/signin";


    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        databaseCleaner.clearDatabase();
    }

    @Test
    @DisplayName("Should return 500 when unexpected exception is thrown on local signup")
    void shouldReturn500WhenUnexpectedExceptionIsThrownOnLocalSignup() throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should return 500 when unexpected exception is thrown on local signin")
    void shouldReturn500WhenUnexpectedExceptionIsThrownOnLocalSignin() throws Exception {
        var requestBody = AuthTestFixture.anyLocalSigninRequest();
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNIN_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isInternalServerError());
    }


    @Test
    @DisplayName("Should return 500 when unexpected exception is thrown on google signup")
    void shouldReturn500WhenUnexpectedExceptionIsThrownOnGoogleSignup() throws Exception {
        String idToken = "any_id_token";
        String json = new ObjectMapper().writeValueAsString(idToken);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.GOOGLE_SIGNUP_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should return 500 when unexpected exception is thrown on google signin")
    void shouldReturn500WhenUnexpectedExceptionIsThrownOnGoogleSignin() throws Exception {
        String idToken = "any_id_token";
        String json = new ObjectMapper().writeValueAsString(idToken);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.GOOGLE_SIGNIN_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isInternalServerError());
    }

}
