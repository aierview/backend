package com.aierview.backend.interview.usecase.impl;

import com.aierview.backend.interview.domain.contract.cache.IInterviewCacheRepository;
import com.aierview.backend.interview.domain.contract.publisher.IInterviewWebSocketPublisher;
import com.aierview.backend.interview.domain.contract.repository.IQuestionRepository;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.exceptions.UnavailableNextQuestionException;
import com.aierview.backend.interview.domain.model.CurrentQuestion;
import com.aierview.backend.interview.usecase.contract.ISendCurrentQuestion;
import com.aierview.backend.shared.testdata.InterviewTestFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class SendCurrentQuestionTests {
    private  ISendCurrentQuestion sendCurrentQuestion;
    private  IInterviewWebSocketPublisher interviewWebSocketPublisher;
    private  IInterviewCacheRepository interviewCacheRepository;
    private  IQuestionRepository questionRepository;


    @BeforeEach
    public void setup() {
        this.interviewWebSocketPublisher = Mockito.mock(IInterviewWebSocketPublisher.class);
        this.interviewCacheRepository = Mockito.mock(IInterviewCacheRepository.class);
        this.questionRepository = Mockito.mock(IQuestionRepository.class);
        sendCurrentQuestion = new SendCurrentQuestion(interviewWebSocketPublisher, interviewCacheRepository, questionRepository);
    }

    @Test
    @DisplayName("Should throw UnavailableNextQuestionException when question not found")
    void shouldThrowUnavailableNextQuestionExceptionWhenQuestionNotFound() {
        CurrentQuestion currentQuestion = new CurrentQuestion(1L,"any_question", "any_audio_url");

        Mockito.when(questionRepository.findById(currentQuestion.questionId())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> sendCurrentQuestion.execute(currentQuestion));

        Assertions.assertThat(exception).isInstanceOf(UnavailableNextQuestionException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("We sorry! We couldn't provide next question. Please try again.");
        Mockito.verify(this.questionRepository, Mockito.times(1)).findById(currentQuestion.questionId());
    }
}
