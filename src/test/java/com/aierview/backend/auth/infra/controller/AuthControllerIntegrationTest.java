package com.aierview.backend.auth.infra.controller;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.enums.AuthProvider;
import com.aierview.backend.auth.domain.model.google.GoogleAuhRequest;
import com.aierview.backend.auth.domain.model.local.LocalSigninRequest;
import com.aierview.backend.auth.infra.persistence.entity.AuthJpaEntity;
import com.aierview.backend.auth.infra.persistence.entity.UserJpaEntity;
import com.aierview.backend.shared.BaseIntegrationTests;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import com.aierview.backend.shared.testdata.HttpServletTestFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthControllerIntegrationTest extends BaseIntegrationTests {

    private final String LOCAL_SIGNUP_API_URL = "/api/v1/auth/local/signup";
    private final String LOCAL_SIGNIN_API_URL = "/api/v1/auth/local/signin";
    private final String GOOGLE_SIGNUP_API_URL = "/api/v1/auth/google/signup";
    private final String GOOGLE_SIGNIN_API_URL = "/api/v1/auth/google/signin";


    @Test
    @Transactional
    @DisplayName("Should return 409 when email is already taken on signup")
    void shouldReturn409WhenEmailIsAlreadyTakenOnSignup() throws Exception {
        UserRef userRef = AuthTestFixture.anyUserRef();
        UserJpaEntity entity = AuthTestFixture.anyUserJpaEntity(userRef);
        entityManager.persist(entity);
        entityManager.flush();

        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("data",
                        Matchers.is("The email "
                                + requestBody.getEmail() + " is already in use.")));
    }

    @NullSource
    @ParameterizedTest
    @DisplayName("Should return 400 when email is null on signup")
    void shouldReturn400WhenEmailIsNullOnSignup(String email) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        requestBody.setEmail(email);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Email is required!")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "any_email", "any_email.com"})
    @DisplayName("Should return 400 when email is in invalid format on signup")
    void shouldReturn400WhenEmailIsInvalidFormatOnSignup(String email) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        requestBody.setEmail(email);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Invalid email format!")));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Should return 400 when name is null on signup")
    void shouldReturn400WhenNameIsNullOnSignup(String name) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        requestBody.setName(name);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Name is required!")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ger", "Gervasio"})
    @DisplayName("Should return 400 when not insert a full name on signup")
    void shouldReturn400WhenNotInsertAFullNameOnSignup(String name) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        requestBody.setName(name);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Please provide your full name!")));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Should return 400 when password is null on signup")
    void shouldReturn400WhenPasswordIsNullOnSignup(String password) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        requestBody.setPassword(password);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Password is required!")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Pass", "Password", "Password123", "123456"})
    @DisplayName("Should return 400 when password is weak on signup")
    void shouldReturn400WhenPasswordIsWeakOnSignup(String password) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        requestBody.setPassword(password);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Password must be at least 6 characters " +
                                "long and must contain at least one uppercase letter, one number, and one special character!")));
    }

    @Test
    @DisplayName("Should return 201 on save success on signup")
    void shouldReturn201WhenSaveSuccessOnSignup() throws Exception {
        var requestBody = AuthTestFixture.anyLocalSignupRequest();
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNUP_API_URL, json);

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("data",
                        Matchers.is("Created")));

    }

    @Test
    @Transactional
    @DisplayName("Should return 401 when user does not exist on signin")
    void shouldReturn401WhenUserDoesNotExistOnSignin() throws Exception {
        var requestBody = AuthTestFixture.anyLocalSigninRequest();
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNIN_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("data",
                        Matchers.is("Email or password is incorrect!")));
    }

    @NullSource
    @ParameterizedTest
    @DisplayName("Should return 400 when email is null on signin")
    void shouldReturn400WhenEmailIsNullOnSignin(String email) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSigninRequest();
        requestBody.setEmail(email);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNIN_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Email is required!")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "any_email", "any_email.com"})
    @DisplayName("Should return 400 when email is in invalid format on signin")
    void shouldReturn400WhenEmailIsInvalidFormatOnSignin(String email) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSigninRequest();
        requestBody.setEmail(email);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNIN_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Invalid email format!")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should return 400 when password is null on signin")
    void shouldReturn400WhenPasswordIsNullOnSignin(String password) throws Exception {
        var requestBody = AuthTestFixture.anyLocalSigninRequest();
        requestBody.setPassword(password);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNIN_API_URL, json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Password is required!")));
    }

    @Test
    @Transactional
    @DisplayName("Should return 200 and set cookies if succeeds on signin")
    void shouldReturn200WhenAndSetCookiesIfSucceedsOnSignin() throws Exception {
        UserJpaEntity userJpaEntity = AuthTestFixture.anyUserJpaEntity();
        entityManager.persist(userJpaEntity);
        entityManager.flush();

        String password = "any_password";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        AuthJpaEntity authJpaEntity = AuthTestFixture.anyAuthJpaEntity(userJpaEntity, hashedPassword);
        entityManager.persist(authJpaEntity);
        entityManager.flush();

        var requestBody = new LocalSigninRequest(userJpaEntity.getEmail(), password);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.LOCAL_SIGNIN_API_URL, json);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("data",
                        Matchers.is("OK")))
                .andExpect(cookie().exists("token"))
                .andExpect(cookie().value("token", Matchers.notNullValue()))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, Matchers.containsString("token=")));

    }

    @Test
    @Transactional
    @DisplayName("Should return 409 when email is already taken on google signup")
    void shouldReturn409WhenEmailIsAlreadyTakenOnGoogleSignup() throws Exception {
        UserRef userRef = AuthTestFixture.anyUserRef();
        UserJpaEntity entity = AuthTestFixture.anyUserJpaEntity(userRef);
        entityManager.persist(entity);
        entityManager.flush();

        GoogleAuhRequest requestBody = new GoogleAuhRequest("any_valid_token");
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.GOOGLE_SIGNUP_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("data",
                        Matchers.is("The email "
                                + userRef.getEmail() + " is already in use.")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should return 400 when id token empty or null on google signup")
    void shouldReturn409WhenIdTokenIsEmptyOrNullTakenOnGoogleSignup(String idToken) throws Exception {
        GoogleAuhRequest requestBody = new GoogleAuhRequest(idToken);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.GOOGLE_SIGNUP_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Id token is required!")));
    }

    @Test
    @DisplayName("Should return 400 when  id token is invalid on google signup")
    void shouldReturn409WhenIdTokenIsInvalidOnGoogleSignup() throws Exception {
        GoogleAuhRequest requestBody = new GoogleAuhRequest("any_invalid_token");
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.GOOGLE_SIGNUP_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Invalid Google account, please provide a valid Google account.")));
    }

    @Test
    @Transactional
    @DisplayName("Should return 201 when signup succeeds on google signup")
    void shouldReturn409WhenSignupSucceedsOnGoogleSignup() throws Exception {
        GoogleAuhRequest requestBody = new GoogleAuhRequest("any_valid_token");
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.GOOGLE_SIGNUP_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("data",
                        Matchers.is("Created")));
    }


    @Test
    @Transactional
    @DisplayName("Should return 401 when user does not exist on google signin")
    void shouldReturn401WhenUserDoesNotExistOnGoogleSignin() throws Exception {
        var requestBody = AuthTestFixture.anyGoogleAuthRequest();
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.GOOGLE_SIGNIN_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("data",
                        Matchers.is("Email or password is incorrect!")));
    }

    @Test
    @DisplayName("Should return 400 when  id token is invalid on google signin")
    void shouldReturn409WhenIdTokenIsInvalidOnGoogleSignIn() throws Exception {
        GoogleAuhRequest requestBody = new GoogleAuhRequest("any_invalid_token");
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.GOOGLE_SIGNIN_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Invalid Google account, please provide a valid Google account.")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should return 400 when id token empty or null on google signin")
    void shouldReturn409WhenEmailIsIdTokenNullTakenOnGoogleSignup(String idToken) throws Exception {
        GoogleAuhRequest requestBody = new GoogleAuhRequest(idToken);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.GOOGLE_SIGNIN_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Id token is required!")));
    }

    @Test
    @Transactional
    @DisplayName("Should return 200 and set cookies if succeeds on google signin")
    void shouldReturn200WhenAndSetCookiesIfSucceedsOnGoogleSignin() throws Exception {
        UserJpaEntity userJpaEntity = AuthTestFixture.anyUserJpaEntity();
        entityManager.persist(userJpaEntity);
        entityManager.flush();

        AuthJpaEntity authJpaEntity = AuthTestFixture.anyAuthJpaEntity(userJpaEntity, null);
        authJpaEntity.setProvider(AuthProvider.GOOGLE);
        entityManager.persist(authJpaEntity);
        entityManager.flush();

        var requestBody = AuthTestFixture.anyGoogleAuthRequest();
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.GOOGLE_SIGNIN_API_URL, json);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("data",
                        Matchers.is("OK")))
                .andExpect(cookie().exists("token"))
                .andExpect(cookie().value("token", Matchers.notNullValue()))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, Matchers.containsString("token=")));

    }
}
