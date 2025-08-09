package com.aierview.backend.interview.usecase.impl;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.interview.domain.enums.InterviewLevel;
import com.aierview.backend.interview.domain.enums.InterviewRole;
import com.aierview.backend.interview.domain.exceptions.UnavailableIAServiceException;
import com.aierview.backend.interview.domain.exceptions.UserNotAuthenticatedException;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;
import com.aierview.backend.interview.usecase.contract.IBeginInterview;
import com.aierview.backend.interview.usecase.contract.IGenerateInterview;
import com.aierview.backend.interview.usecase.contract.IGetLoggedUser;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import com.aierview.backend.shared.testdata.InterviewTestFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BeginInterviewTests {
    private IBeginInterview beginInterview;
    private IGetLoggedUser getLoggedUser;
    private IGenerateInterview generateInterview;

    @BeforeEach
    void setUp() {
        this.getLoggedUser = mock(IGetLoggedUser.class);
        this.generateInterview = mock(IGenerateInterview.class);
        this.beginInterview = new BeginInterview(getLoggedUser,generateInterview);
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

        when(this.getLoggedUser.execute()).thenReturn(savedUser);
        when(this.generateInterview.execute(request,savedUser)).thenThrow(new UnavailableIAServiceException());

        Throwable exception = Assertions.catchThrowable(() -> this.beginInterview.execute(request));

        assertThat(exception).isInstanceOf(UnavailableIAServiceException.class);
        assertThat(exception.getMessage()).isEqualTo("We sorry! IA Service not available at this time, please try again later.");
        verify(this.getLoggedUser, times(1)).execute();
        verify(this.generateInterview, times(1)).execute(request,savedUser);
    }
}
