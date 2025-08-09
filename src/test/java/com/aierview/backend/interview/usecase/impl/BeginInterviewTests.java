package com.aierview.backend.interview.usecase.impl;

import com.aierview.backend.interview.domain.enums.InterviewLevel;
import com.aierview.backend.interview.domain.enums.InterviewRole;
import com.aierview.backend.interview.domain.exceptions.UserNotAuthenticatedException;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;
import com.aierview.backend.interview.usecase.contract.IBeginInterview;
import com.aierview.backend.interview.usecase.contract.IGetLoggedUser;
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

    @BeforeEach
    void setUp() {
        this.getLoggedUser = mock(IGetLoggedUser.class);
        this.beginInterview = new BeginInterview(getLoggedUser);
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
}
