package com.aierview.backend.interview.usecase.impl;

import com.aierview.backend.interview.domain.contract.cache.IInterviewCacheRepository;
import com.aierview.backend.interview.domain.contract.publisher.IInterviewWebSocketPublisher;
import com.aierview.backend.interview.domain.contract.repository.IQuestionRepository;
import com.aierview.backend.interview.domain.exceptions.UnavailableNextQuestionException;
import com.aierview.backend.interview.domain.model.OnAnswerReceivedRequest;
import com.aierview.backend.interview.usecase.contract.IOnAnswerReceived;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class OnAnswerReceivedTests {
    private  IOnAnswerReceived onAnswerReceived;
    private IQuestionRepository questionRepository;
    private IInterviewCacheRepository interviewCacheRepository;
    private IInterviewWebSocketPublisher interviewWebSocketPublisher;

    @BeforeEach
    void setUp() {
        this.questionRepository = Mockito.mock(IQuestionRepository.class);
        this.interviewCacheRepository = Mockito.mock(IInterviewCacheRepository.class);
        this.interviewWebSocketPublisher = Mockito.mock(IInterviewWebSocketPublisher.class);
        this.onAnswerReceived =  new OnAnswerReceived(questionRepository, interviewCacheRepository, interviewWebSocketPublisher);
    }


    @Test
    @DisplayName("Should throw UnavailableNextQuestionException if user does not exists")
    void shouldThrowUnavailableNextQuestionException() {
        OnAnswerReceivedRequest request = new OnAnswerReceivedRequest("any_answer", 1L);
        Mockito.when(this.questionRepository.findById(request.questionId())).thenReturn(Optional.empty());
        Throwable exception = Assertions.catchThrowable(() -> this.onAnswerReceived.execute(request));
        Assertions.assertThat(exception).isInstanceOf(UnavailableNextQuestionException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("We sorry! We couldn't provide next question. Please try again.");
    }
}
