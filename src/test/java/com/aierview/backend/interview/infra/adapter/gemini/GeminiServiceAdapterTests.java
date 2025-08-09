package com.aierview.backend.interview.infra.adapter.gemini;

import com.aierview.backend.interview.domain.contract.IA.IGenerateQuestions;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.domain.exceptions.UnavailableIAServiceException;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;
import com.aierview.backend.shared.testdata.InterviewTestFixture;
import com.aierview.backend.shared.utils.GeminiFunctUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class GeminiServiceAdapterTests {
    private IGenerateQuestions generateQuestions;
    private GeminiFunctUtils geminiFunctUtils;

    @BeforeEach
    void setUp() {
        this.geminiFunctUtils =  Mockito.mock(GeminiFunctUtils.class);
        this.generateQuestions =  new GeminiServiceAdapter(geminiFunctUtils);
    }

    @Test
    @DisplayName("Should throw UnavailableIAServiceException if get response throw")
    void shouldThrowUnavailableIAServiceExceptionIfGetResponseThrows() {
        BeginInterviewRequest request = InterviewTestFixture.anyBeginInterviewRequest();
        Long interviewId = 1L;
        String prompt =  InterviewTestFixture.generateQuestionsPrompt(request);

        Mockito.when(this.geminiFunctUtils.generateQuestionsPrompt(request)).thenReturn(prompt);
        Mockito.when(this.geminiFunctUtils.getResponse(prompt)).thenThrow(new RuntimeException("Any_exception"));

        Throwable exception = Assertions.catchThrowable(() -> this.generateQuestions.execute(request, interviewId));

        Assertions.assertThat(exception).isInstanceOf(UnavailableIAServiceException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("We sorry! IA Service not available at this time, please try again later.");
        Mockito.verify(this.geminiFunctUtils, Mockito.times(1)).generateQuestionsPrompt(request);
        Mockito.verify(geminiFunctUtils, Mockito.times(1)).getResponse(prompt);
    }

    @Test
    @DisplayName("Should generate questions")
    void shouldGenerateQuestions() {
        BeginInterviewRequest request = InterviewTestFixture.anyBeginInterviewRequest();
        Interview interviewRef = Interview.builder().id(1L).build();

        String prompt =  InterviewTestFixture.generateQuestionsPrompt(request);

        List<String> questionsText = InterviewTestFixture.anyQuestionsStringList();

        Mockito.when(this.geminiFunctUtils.generateQuestionsPrompt(request)).thenReturn(prompt);
        Mockito.when(this.geminiFunctUtils.getResponse(prompt)).thenReturn(questionsText);

        List<Question> result =  this.generateQuestions.execute(request,interviewRef.getId());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(questionsText.size()-1);
        Assertions.assertThat(result.getFirst().getQuestion()).isEqualTo(questionsText.getFirst());
        Assertions.assertThat(result.getLast().getQuestion()).isEqualTo(questionsText.get(questionsText.size()-2));
        Mockito.verify(this.geminiFunctUtils, Mockito.times(1)).generateQuestionsPrompt(request);
        Mockito.verify(geminiFunctUtils, Mockito.times(1)).getResponse(prompt);

    }
}
