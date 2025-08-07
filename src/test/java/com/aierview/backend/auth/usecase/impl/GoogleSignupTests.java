package com.aierview.backend.auth.usecase.impl;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.exceptions.EmailAlreadyInUseException;
import com.aierview.backend.auth.domain.exceptions.InvalidGoogleIdTokenException;
import com.aierview.backend.auth.domain.google.IExtractUserDetails;
import com.aierview.backend.auth.domain.repository.IUserRepository;
import com.aierview.backend.auth.usecase.contract.IGoogleSignup;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class GoogleSignupTests {
    private IGoogleSignup googleSignup;
    private IExtractUserDetails extractUserDetails;

    @BeforeEach
    void setup() {
        this.extractUserDetails = Mockito.mock(IExtractUserDetails.class);
        this.googleSignup =  new GoogleSignup(extractUserDetails);
    }

    @Test
    @DisplayName("Should throw InvalidGoogleIdTokenException when Google account is invalid")
    void shouldThrowInvalidGoogleIdTokenExceptionWhenGoogleAccountIsInvalid() {
        String idToken =  "any_token";

        Mockito.when(this.extractUserDetails.extract(idToken)).thenReturn(Optional.empty());

        Throwable  exception = Assertions.catchThrowable(() -> this.googleSignup.execute(idToken));

        Assertions.assertThat(exception).isInstanceOf(InvalidGoogleIdTokenException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("Invalid Google account, please provide a valid Google account.");
        Mockito.verify(this.extractUserDetails, Mockito.times(1)).extract(idToken);
    }
}
