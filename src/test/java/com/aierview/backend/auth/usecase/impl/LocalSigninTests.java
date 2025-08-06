package com.aierview.backend.auth.usecase.impl;

import com.aierview.backend.auth.domain.exceptions.InvalidCredentialException;
import com.aierview.backend.auth.domain.model.LocalSigninRequest;
import com.aierview.backend.auth.domain.repository.IUserRepository;
import com.aierview.backend.auth.usecase.contract.ILocalSignin;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LocalSigninTests {
    private ILocalSignin localSignin;
    private IUserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.userRepository = mock(IUserRepository.class);
        this.localSignin =  new LocalSignin(this.userRepository);
    }


    @Test
    @DisplayName("Should throw InvalidCredentialException when user does not exists on find by email ")
    void shouldThrowInvalidCredentialExceptionWhenUserDoesNotExistsOnFindByEmail() {
        LocalSigninRequest request = AuthTestFixture.anyLocalSigninRequest();

        when(this.userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        Throwable exception = catchThrowable(() -> this.localSignin.execute(request));

        assertThat(exception).isInstanceOf(InvalidCredentialException.class);
        assertThat(exception.getMessage()).isEqualTo("Email or password is incorrect!");
        verify(this.userRepository, times(1)).findByEmail(request.getEmail());
    }
}
