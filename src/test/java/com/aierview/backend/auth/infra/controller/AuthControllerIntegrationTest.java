package com.aierview.backend.auth.infra.controller;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.infra.persisntence.jpa.entity.UserJpaEntity;
import com.aierview.backend.auth.usecase.contract.ILocalSignup;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import com.aierview.backend.shared.testdata.HttpServletTestFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthControllerIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private final String URL = "/api/v1/auth";
    private final String LOCAL_SIGNUP_API_URL = "/api/v1/auth/local/signup";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ILocalSignup localSignup;

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
    }

    @Test
    @DisplayName("Should return 500 when unexpected exception is thrown")
    void shouldReturn500WhenUnexpectedExceptionIsThrown() throws Exception {
        postgresContainer.stop();
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Transactional
    @DisplayName("Should return 409 when email is already taken")
    void shouldReturn409WhenEmailIsAlreadyTaken() throws Exception {
        UserRef userRef =  AuthTestFixture.anyUserRef();
        UserJpaEntity entity =  AuthTestFixture.anyUserJpaEntity(userRef);
        entityManager.persist(entity);
        entityManager.flush();

        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("data",
                        Matchers.is("The email "
                                + requestBody.getEmail() + " is already in use.")));
    }

    @NullSource
    @ParameterizedTest
    @DisplayName("Should return 400 when email is null")
    void shouldReturn400WhenEmailIsNull(String email) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        requestBody.setEmail(email);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Email is required!")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "any_email", "any_email.com"})
    @DisplayName("Should return 400 when email is in invalid format")
    void shouldReturn400WhenEmailIsInvalidFormat(String email) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        requestBody.setEmail(email);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Invalid email format!")));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Should return 400 when name is null")
    void shouldReturn400WhenNameIsNull(String name) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        requestBody.setName(name);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Name is required!")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ger", "Gervasio"})
    @DisplayName("Should return 400 when not insert a full name")
    void shouldReturn400WhenNotInsertAFullName(String name) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        requestBody.setName(name);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Please provide your full name!")));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Should return 400 when password is null")
    void shouldReturn400WhenPasswordIsNull(String password) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        requestBody.setPassword(password);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Password is required!")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Pass", "Password", "Password123", "123456"})
    @DisplayName("Should return 400 when password is weak")
    void shouldReturn400WhenPasswordIsWeak(String password) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        requestBody.setPassword(password);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Password must be at least 6 characters " +
                                "long and must contain at least one uppercase letter, one number, and one special character!")));
    }
}
