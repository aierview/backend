package com.aierview.backend.auth.usecase.impl;

import com.aierview.backend.auth.domain.contact.repository.IAuthRepository;
import com.aierview.backend.auth.domain.contact.repository.IUserRepository;
import com.aierview.backend.auth.domain.contact.security.IPasswordEncoder;
import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.exceptions.EmailAlreadyInUseException;
import com.aierview.backend.auth.domain.model.LocalSignupRequest;
import com.aierview.backend.auth.usecase.contract.ILocalSignup;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public class LocalSignupTests {
    private ILocalSignup localSignup;
    private IUserRepository userRepository;
    private IPasswordEncoder passwordEncoder;
    private IAuthRepository authRepository;

    @BeforeEach
    void setUp() {
        this.userRepository = mock(IUserRepository.class);
        this.passwordEncoder = mock(IPasswordEncoder.class);
        this.authRepository = mock(IAuthRepository.class);
        this.localSignup = new LocalSignup(userRepository, passwordEncoder, authRepository);
    }

    @Test
    @DisplayName("Should throw EmailAlreadyInUseException if email is already in use")
    void shouldThrowEmailAlreadyInUseExceptionIfEmailIsAlreadyInUse() {
        LocalSignupRequest request = AuthTestFixture.anyLocalSignupRequest();
        UserRef existingUser = AuthTestFixture.anySavedUserRef();

        when(this.userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        Throwable exception = catchThrowable(() -> this.localSignup.execute(request));

        assertThat(exception).isInstanceOf(EmailAlreadyInUseException.class);
        assertThat(exception.getMessage()).isEqualTo("The email " + request.getEmail() + " is already in use.");
        verify(this.userRepository, times(1)).findByEmail(request.getEmail());
    }

    @Test
    @DisplayName("Should save user ref, encode the password and save auth details")
    void shouldSaveUserRefAndEncodePasswordAndSaveAuthDetails() {
        LocalSignupRequest request = AuthTestFixture.anyLocalSignupRequest();
        UserRef toSaveUser = AuthTestFixture.anyUserRef();
        UserRef savedUser = AuthTestFixture.anySavedUserRef();
        Auth toSaveAuth = AuthTestFixture.anyAuth(savedUser);
        Auth savedAuth = AuthTestFixture.anySavedAuth(savedUser);

        when(this.userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(this.userRepository.save(toSaveUser)).thenReturn(savedUser);
        when(this.passwordEncoder.encode(request.getPassword())).thenReturn(toSaveAuth.getPassword());
        when(this.authRepository.save(toSaveAuth)).thenReturn(savedAuth);

        this.localSignup.execute(request);

        verify(this.userRepository, times(1)).findByEmail(request.getEmail());
        verify(this.userRepository, times(1)).save(toSaveUser);
        verify(this.passwordEncoder, times(1)).encode(request.getPassword());
        verify(this.authRepository, times(1)).save(toSaveAuth);
    }

}
