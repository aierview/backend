package com.aierview.backend.auth.infra.controller;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.infra.persisntence.jpa.entity.UserJpaEntity;
import com.aierview.backend.shared.BaseIntegrationTests;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import com.aierview.backend.shared.testdata.HttpServletTestFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerIntegrationTest extends BaseIntegrationTests {

    //    LOCAL SIGNUP TESTES
    private final String LOCAL_SIGNUP_API_URL = "/api/v1/auth/local/signup";

    @Test
    @Transactional
    @DisplayName("Should return 409 when email is already taken")
    void shouldReturn409WhenEmailIsAlreadyTaken() throws Exception {
        UserRef userRef = AuthTestFixture.anyUserRef();
        UserJpaEntity entity = AuthTestFixture.anyUserJpaEntity(userRef);
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

    @Test
    @DisplayName("Should return 201 on save success")
    void shouldReturn201WhenSaveSuccess() throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("data",
                        Matchers.is("Created")));

    }

//    LOCAL SIGIN IN TESTS
    private final String LOCAL_SIGNIN_API_URL = "/api/v1/auth/local/signin";

    @Test
    @Transactional
    @DisplayName("Should return 401 when user does not exist on signin")
    void shouldReturn401WhenUserDoesNotExistOnSignin() throws Exception {
        var requestBody = AuthTestFixture.anyLocalSigninRequest();
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyMockMvcRequestBuilder(this.LOCAL_SIGNIN_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("data",
                        Matchers.is("Email or password is incorrect!")));
    }
}
