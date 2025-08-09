package com.aierview.backend.interview.usecase.impl;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.interview.domain.contract.repository.InterviewRepository;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.domain.exceptions.UnavailableIAServiceException;
import com.aierview.backend.interview.domain.exceptions.UserNotAuthenticatedException;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;
import com.aierview.backend.interview.usecase.contract.IBeginInterview;
import com.aierview.backend.interview.usecase.contract.IGenerateQuestions;
import com.aierview.backend.interview.usecase.contract.IGetLoggedUser;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import com.aierview.backend.shared.testdata.InterviewTestFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BeginInterviewTests {
    private IBeginInterview beginInterview;
    private IGetLoggedUser getLoggedUser;
    private InterviewRepository interviewRepository;
    private IGenerateQuestions generateQuestions;

    @BeforeEach
    void setUp() {
        this.getLoggedUser = mock(IGetLoggedUser.class);
        this.interviewRepository = mock(InterviewRepository.class);
        this.generateQuestions = mock(IGenerateQuestions.class);
        this.beginInterview = new BeginInterview(getLoggedUser, interviewRepository,generateQuestions);
    }

    @Test
    @DisplayName("Should throw UserNotAuthenticatedException if user is not authenticate")
    void shouldThrowUserNotAuthenticatedExceptionIfUserIsNotAuthenticated() {
        BeginInterviewRequest request = InterviewTestFixture.anyBeginInterviewRequest();

        when(this.getLoggedUser.execute()).thenThrow(new UserNotAuthenticatedException());

        Throwable exception = Assertions.catchThrowable(() -> this.beginInterview.execute(request));

        assertThat(exception).isInstanceOf(UserNotAuthenticatedException.class);
        assertThat(exception.getMessage()).isEqualTo("User is not authenticated!");
        verify(this.getLoggedUser, times(1)).execute();
    }

    @Test
    @DisplayName("Should throw UnavailableIAServiceException if generate interview throws")
    void shouldThrowUnavailableIAServiceExceptionIfGenerateInterviewThrows() {
        BeginInterviewRequest request = InterviewTestFixture.anyBeginInterviewRequest();
        UserRef savedUser = AuthTestFixture.anySavedUserRef();

        Interview interviewWithNoQuestion =  InterviewTestFixture.anyInterviewWithNoQuestions(savedUser);
        Interview savedInterviewWithNoQuestion = InterviewTestFixture.anySavedInterviewWithNoQuestions(interviewWithNoQuestion);

        when(this.getLoggedUser.execute()).thenReturn(savedUser);
        when(this.interviewRepository.save(interviewWithNoQuestion)).thenReturn(savedInterviewWithNoQuestion);
        when(this.generateQuestions.execute(request,savedInterviewWithNoQuestion.getId())).thenThrow(new UnavailableIAServiceException());

        Throwable exception = Assertions.catchThrowable(() -> this.beginInterview.execute(request));

        assertThat(exception).isInstanceOf(UnavailableIAServiceException.class);
        assertThat(exception.getMessage()).isEqualTo("We sorry! IA Service not available at this time, please try again later.");
        verify(this.getLoggedUser, times(1)).execute();
        verify(this.generateQuestions, times(1)).execute(request, savedInterviewWithNoQuestion.getId());
    }

//    @Test
//    @DisplayName("Should publish first question when begin interview succeeds")
//    void shouldPublishFirstQuestionWhenBeginInterviewSucceeds() {
//        BeginInterviewRequest request = InterviewTestFixture.anyBeginInterviewRequest();
//        UserRef savedUser = AuthTestFixture.anySavedUserRef();
//        List<Question> questionList =  InterviewTestFixture.anyQuestionList();
//
//        when(this.getLoggedUser.execute()).thenReturn(savedUser);
//        when(this.generateQuestions.execute(request)).thenReturn(questionList);
//
//
//        Throwable exception = Assertions.catchThrowable(() -> this.beginInterview.execute(request));
//
//        assertThat(exception).isInstanceOf(UnavailableIAServiceException.class);
//        assertThat(exception.getMessage()).isEqualTo("We sorry! IA Service not available at this time, please try again later.");
//        verify(this.getLoggedUser, times(1)).execute();
//        verify(this.generateQuestions, times(1)).execute(request);
//
//    }
}
