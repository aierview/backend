package com.aierview.backend.interview.usecase.impl;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.interview.domain.contract.cache.IInterviewCacheRepository;
import com.aierview.backend.interview.domain.contract.publisher.IInterviewEventPublisher;
import com.aierview.backend.interview.domain.contract.repository.IQuestionRepository;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.entity.InterviewState;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.domain.exceptions.UnavailableNextQuestionException;
import com.aierview.backend.interview.domain.model.InterviewEventPublisherPayload;
import com.aierview.backend.interview.domain.model.OnQuestionReceivedRequest;
import com.aierview.backend.interview.usecase.contract.IOnQuestionReceived;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import com.aierview.backend.shared.testdata.InterviewTestFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

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
        onQuestionReceived = new OnQuestionReceived(questionRepository, interviewCacheRepository, interviewEventPublisher);
    }

    @Test
    @DisplayName("Should throw UnavailableNextQuestionException when question not found")
    void shouldThrowUnavailableNextQuestionExceptionWhenQuestionNotFound() {
        OnQuestionReceivedRequest request = InterviewTestFixture.anyOnQuestionReceivedRequest();

        when(questionRepository.findById(request.questionId())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> this.onQuestionReceived.execute(request));

        Assertions.assertThat(exception).isInstanceOf(UnavailableNextQuestionException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("We sorry! We couldn't provide next question. Please try again.");
        verify(this.questionRepository, times(1)).findById(request.questionId());
    }

    @Test
    @DisplayName("Should not publish question when there is no next question")
    void shouldNotPublishQuestionWhenThereIsNoNextQuestion() {
        UserRef savedUser = AuthTestFixture.anySavedUserRef();

        Interview toSaveInterview = InterviewTestFixture.anyInterviewWithNoQuestions(savedUser);
        Interview savedInterview = InterviewTestFixture.anySavedInterviewWithNoQuestions(toSaveInterview);

        Question question = InterviewTestFixture.anySavedQuestion(savedInterview);
        OnQuestionReceivedRequest request = InterviewTestFixture.anyOnQuestionReceivedRequest();

        InterviewState interviewState = InterviewTestFixture.anySavedInterviewState(savedInterview, question);

        when(this.questionRepository.findById(request.questionId())).thenReturn(Optional.of(question));
        when(this.interviewCacheRepository.get(savedInterview.getId())).thenReturn(interviewState);

        this.onQuestionReceived.execute(request);
        verify(this.questionRepository, times(1)).findById(request.questionId());
        verify(this.interviewCacheRepository, times(1)).get(savedInterview.getId());
        verify(this.interviewCacheRepository, times(1)).revalidate(savedInterview.getId(), interviewState);
        verify(this.interviewEventPublisher, times(0)).publish(any(InterviewEventPublisherPayload.class));
    }

    @Test
    @DisplayName("Should publish next question when exists")
    void shouldPublishNextQuestionWhenPublishNextQuestion() {
        UserRef savedUser = AuthTestFixture.anySavedUserRef();

        Interview toSaveInterview = InterviewTestFixture.anyInterviewWithNoQuestions(savedUser);
        Interview savedInterview = InterviewTestFixture.anySavedInterviewWithNoQuestions(toSaveInterview);

        Question question = InterviewTestFixture.anySavedQuestion(savedInterview);
        OnQuestionReceivedRequest request = InterviewTestFixture.anyOnQuestionReceivedRequest();
        Question questionWihAudioUrl = InterviewTestFixture.anySavedQuestion(question, "any_audio_url");
        Question questionWihAudioUrl1 = InterviewTestFixture.anySavedQuestion(question, "any_audio_url");

        InterviewState interviewState = InterviewTestFixture.anySavedInterviewState(savedInterview, question);
        InterviewState interviewStateWFCACK = InterviewTestFixture
                .anySavedInterviewState(interviewState, List.of(questionWihAudioUrl, questionWihAudioUrl1));
        interviewStateWFCACK.setCurrentQuestionIndex(0);

        when(this.questionRepository.findById(request.questionId())).thenReturn(Optional.of(question));
        when(this.interviewCacheRepository.get(savedInterview.getId())).thenReturn(interviewState);

        this.onQuestionReceived.execute(request);
        verify(this.questionRepository, times(1)).findById(request.questionId());
        verify(this.interviewCacheRepository, times(1)).get(savedInterview.getId());

    }
}
