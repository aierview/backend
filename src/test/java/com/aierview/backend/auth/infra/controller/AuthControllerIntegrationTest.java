package com.aierview.backend.auth.infra.controller;

import com.aierview.backend.auth.usecase.contract.ILocalSignup;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
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

    @NullSource
    @ParameterizedTest
    @DisplayName("Should return 400 when email is null")
    void shouldReturn400WhenEmailIsNull(String email) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        requestBody.setEmail(email);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(this.URL + "/local/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Email is required!")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"","any_email","any_email.com"})
    @DisplayName("Should return 400 when email is in invalid format")
    void shouldReturn400WhenEmailIsInvalidFormat(String email) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        requestBody.setEmail(email);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(this.URL + "/local/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Invalid email format!")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should return 400 when name is null or empty")
    void shouldReturn400WhenNameIsNullOrEmpty(String name) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
       requestBody.setName(name);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(this.URL + "/local/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Name is required!")));
    }
}
