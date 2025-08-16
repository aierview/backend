package com.aierview.backend.interview.usecase.impl;

import com.aierview.backend.interview.domain.contract.cache.IInterviewCacheRepository;
import com.aierview.backend.interview.domain.contract.publisher.IInterviewEventPublisher;
import com.aierview.backend.interview.domain.contract.publisher.IInterviewWebSocketPublisher;
import com.aierview.backend.interview.domain.contract.repository.IQuestionRepository;
import com.aierview.backend.interview.domain.exceptions.UnavailableNextQuestionException;
import com.aierview.backend.interview.domain.model.CurrentQuestion;
import com.aierview.backend.interview.domain.model.OnQuestionReceivedRequest;
import com.aierview.backend.interview.usecase.contract.IOnQuestionReceived;
import com.aierview.backend.interview.usecase.contract.ISendCurrentQuestion;
import com.aierview.backend.shared.testdata.InterviewTestFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class OnQuestionReceivedTests {
    private IOnQuestionReceived onQuestionReceived;
    private IQuestionRepository questionRepository;
    private IInterviewCacheRepository interviewCacheRepository;
    private IInterviewEventPublisher interviewEventPublisher;


    @BeforeEach
    public void setup() {
        this.questionRepository = mock(IQuestionRepository.class);
        this.interviewCacheRepository = mock(IInterviewCacheRepository.class);
        this.interviewEventPublisher = mock(IInterviewEventPublisher.class);
        onQuestionReceived = new OnQuestionReceived(questionRepository,interviewCacheRepository,interviewEventPublisher);
    }

    @Test
    @DisplayName("Should throw UnavailableNextQuestionException when question not found")
    void shouldThrowUnavailableNextQuestionExceptionWhenQuestionNotFound() {
        OnQuestionReceivedRequest  request = InterviewTestFixture.anyOnQuestionReceivedRequest();

        when(questionRepository.findById(request.questionId())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> this.onQuestionReceived.execute(request));

        Assertions.assertThat(exception).isInstanceOf(UnavailableNextQuestionException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("We sorry! We couldn't provide next question. Please try again.");
        verify(this.questionRepository, times(1)).findById(request.questionId());
    }
}
