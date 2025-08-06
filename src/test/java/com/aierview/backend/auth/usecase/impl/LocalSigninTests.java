package com.aierview.backend.auth.usecase.impl;

import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.exceptions.InvalidCredentialException;
import com.aierview.backend.auth.domain.model.LocalSigninRequest;
import com.aierview.backend.auth.domain.repository.IAuthRepository;
import com.aierview.backend.auth.domain.repository.IUserRepository;
import com.aierview.backend.auth.domain.security.IPasswordComparer;
import com.aierview.backend.auth.usecase.contract.ILocalSignin;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public class LocalSigninTests {
    private ILocalSignin localSignin;
    private IUserRepository userRepository;
    private IAuthRepository authRepository;
    private IPasswordComparer passwordComparer;

    @BeforeEach
    void setUp() {
        this.userRepository = mock(IUserRepository.class);
        this.authRepository = mock(IAuthRepository.class);
        this.passwordComparer = mock(IPasswordComparer.class);
        this.localSignin = new LocalSignin(this.userRepository, authRepository, passwordComparer);
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

    @Test
    @DisplayName("Should throw InvalidCredentialsException when password does not match")
    void shouldThrowInvalidCredentialsExceptionWhenPasswordDoesNotMatch() {
        LocalSigninRequest request = AuthTestFixture.anyLocalSigninRequest();
        UserRef savedUser = AuthTestFixture.anySavedUserRef();
        Auth savedAuth = AuthTestFixture.anySavedAuth(savedUser);

        when(this.userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(savedUser));
        when(this.authRepository.findByUserId(savedUser.getId())).thenReturn(Optional.of(savedAuth));
        when(this.passwordComparer.matches(request.getPassword(), savedAuth.getPassword())).thenReturn(false);

        Throwable exception = catchThrowable(() -> this.localSignin.execute(request));

        assertThat(exception).isInstanceOf(InvalidCredentialException.class);
        assertThat(exception.getMessage()).isEqualTo("Email or password is incorrect!");
        verify(this.userRepository, times(1)).findByEmail(request.getEmail());
        verify(this.authRepository, times(1)).findByUserId(savedUser.getId());
        verify(this.passwordComparer, times(1)).matches(request.getPassword(), savedAuth.getPassword());
    }
}
