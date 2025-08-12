package com.aierview.backend.shared.utils;

import com.aierview.backend.auth.infra.adapter.token.JwtTokenAdapter;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;
import com.aierview.backend.shared.testdata.InterviewTestFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

public class GeminiFunctUtilsTests {
    private final String apikey = "any_api_key";
    private final String apiUrl = "any_api_url";

    private GeminiFunctUtils geminiFunctUtils;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        this.restTemplate =  Mockito.mock(RestTemplate.class);
        this.geminiFunctUtils = new GeminiFunctUtils(restTemplate);

        var apikeyField = GeminiFunctUtils.class.getDeclaredField("apiKey");
        apikeyField.setAccessible(true);
        apikeyField.set(geminiFunctUtils, apikey);

        var apiUrlField = GeminiFunctUtils.class.getDeclaredField("apiUrl");
        apiUrlField.setAccessible(true);
        apiUrlField.set(geminiFunctUtils, apiUrl);
    }

    @Test
    @DisplayName("Should return question prompt with correct role, stack and level")
    void shouldReturnQuestionPromptWithCorrectRoleStackAndLevel() {
        BeginInterviewRequest request = InterviewTestFixture.anyBeginInterviewRequest();
        String prompt =  InterviewTestFixture.generateQuestionsPrompt(request);

        String result = this.geminiFunctUtils.generateQuestionsPrompt(request);
        Assertions.assertEquals(result, prompt);
    }
}
