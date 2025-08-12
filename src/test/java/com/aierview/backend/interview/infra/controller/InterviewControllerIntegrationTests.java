package com.aierview.backend.interview.infra.controller;

import com.aierview.backend.shared.BaseIntegrationTests;
import com.aierview.backend.shared.testdata.HttpServletTestFixture;
import com.aierview.backend.shared.testdata.InterviewTestFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InterviewControllerIntegrationTests extends BaseIntegrationTests {
    private final String BEGIN_INTERVIEW_API_URL = "/api/v1/interview/begin";


    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should return 400 when stack is null or empty")
    void shouldReturn400WhenStackIsNullOrEmpty(String stack) throws Exception {

        var requestBody = InterviewTestFixture.anyBeginInterviewRequest();
        requestBody.setStack(stack);
        String json = new ObjectMapper().writeValueAsString(requestBody);

        MockHttpServletRequestBuilder request = HttpServletTestFixture
                .anyPostMockMvcRequestBuilder(this.BEGIN_INTERVIEW_API_URL, json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data",
                        Matchers.is("Stack is required!")));
    }
}
