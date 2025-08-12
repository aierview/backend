package com.aierview.backend.shared.utils;

import com.aierview.backend.interview.domain.exceptions.UnavailableIAServiceException;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;
import com.aierview.backend.shared.testdata.InterviewTestFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GeminiFunctUtilsTests {
    private final String apiKey = "any_api_key";
    private final String apiUrl = "any_api_url";

    private GeminiFunctUtils geminiFunctUtils;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        this.restTemplate = mock(RestTemplate.class);
        this.geminiFunctUtils = new GeminiFunctUtils(restTemplate);

        var apikeyField = GeminiFunctUtils.class.getDeclaredField("apiKey");
        apikeyField.setAccessible(true);
        apikeyField.set(geminiFunctUtils, apiKey);

        var apiUrlField = GeminiFunctUtils.class.getDeclaredField("apiUrl");
        apiUrlField.setAccessible(true);
        apiUrlField.set(geminiFunctUtils, apiUrl);
    }

    @Test
    @DisplayName("Should return question prompt with correct role, stack and level")
    void shouldReturnQuestionPromptWithCorrectRoleStackAndLevel() {
        BeginInterviewRequest request = InterviewTestFixture.anyBeginInterviewRequest();
        String prompt = InterviewTestFixture.generateQuestionsPrompt(request);

        String result = this.geminiFunctUtils.generateQuestionsPrompt(request);
        Assertions.assertEquals(result, prompt);
    }

    @Test
    @DisplayName("Should throw UnavailableIAServiceException when response is null")
    void shouldThrowUnavailableIAServiceExceptionWhenResponseIsNull() {
        BeginInterviewRequest request = InterviewTestFixture.anyBeginInterviewRequest();
        String prompt = InterviewTestFixture.generateQuestionsPrompt(request);

        when(this.restTemplate.postForObject(any(URI.class), any(HttpEntity.class), eq(Map.class))).thenReturn(null);

        Throwable exception = catchThrowable(() -> this.geminiFunctUtils.getResponse(prompt));

        assertThat(exception).isInstanceOf(UnavailableIAServiceException.class);
        assertThat(exception.getMessage()).isEqualTo("We sorry! IA Service not available at this time, please try again later.");
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject(any(URI.class), any(HttpEntity.class), eq(Map.class));
    }


    @Test
    @DisplayName("Should throw UnavailableIAServiceException when response has no candidates")
    void shouldThrowUnavailableIAServiceExceptionWhenResponseHasNoCandidates() {
        BeginInterviewRequest request = InterviewTestFixture.anyBeginInterviewRequest();
        String prompt = InterviewTestFixture.generateQuestionsPrompt(request);

        Map<String, Object> fakeResponse = new HashMap<>();
        fakeResponse.put("foo", "bar");

        when(this.restTemplate.postForObject(any(URI.class), any(HttpEntity.class), eq(Map.class))).thenReturn(fakeResponse);

        Throwable exception = catchThrowable(() -> this.geminiFunctUtils.getResponse(prompt));

        assertThat(exception).isInstanceOf(UnavailableIAServiceException.class);
        assertThat(exception.getMessage()).isEqualTo("We sorry! IA Service not available at this time, please try again later.");
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject(any(URI.class), any(HttpEntity.class), eq(Map.class));
    }

    @Test
    @DisplayName("Should throw UnavailableIAServiceException when response candidates is empty")
    void shouldThrowUnavailableIAServiceExceptionWhenResponseCandidatesIsEmpty() {
        BeginInterviewRequest request = InterviewTestFixture.anyBeginInterviewRequest();
        String prompt = InterviewTestFixture.generateQuestionsPrompt(request);

        Map<String, Object> fakeResponse = new HashMap<>();
        fakeResponse.put("candidates", List.of());

        when(this.restTemplate.postForObject(any(URI.class), any(HttpEntity.class), eq(Map.class))).thenReturn(fakeResponse);

        Throwable exception = catchThrowable(() -> this.geminiFunctUtils.getResponse(prompt));

        assertThat(exception).isInstanceOf(UnavailableIAServiceException.class);
        assertThat(exception.getMessage()).isEqualTo("We sorry! IA Service not available at this time, please try again later.");
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject(any(URI.class), any(HttpEntity.class), eq(Map.class));
    }
}
