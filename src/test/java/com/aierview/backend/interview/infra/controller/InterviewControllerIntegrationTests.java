package com.aierview.backend.interview.infra.controller;

import com.aierview.backend.auth.domain.model.local.LocalSigninRequest;
import com.aierview.backend.auth.infra.persistence.entity.AuthJpaEntity;
import com.aierview.backend.auth.infra.persistence.entity.UserJpaEntity;
import com.aierview.backend.shared.BaseIntegrationTests;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import com.aierview.backend.shared.testdata.HttpServletTestFixture;
import com.aierview.backend.shared.testdata.InterviewTestFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class InterviewControllerIntegrationTests extends BaseIntegrationTests {
    private final String LOCAL_SIGNIN_API_URL = "/api/v1/auth/local/signin";
    private final String BEGIN_INTERVIEW_API_URL = "/api/v1/interview/begin";


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
