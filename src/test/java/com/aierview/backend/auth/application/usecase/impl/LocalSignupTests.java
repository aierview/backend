package com.aierview.backend.auth.application.usecase.impl;

import com.aierview.backend.auth.application.usecase.contract.ILocalSignup;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.exceptions.EmailAlreadyInUseException;
import com.aierview.backend.auth.domain.model.LocalSigninRequest;
import com.aierview.backend.auth.domain.repository.IUserRepository;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LocalSignupTests {
    private ILocalSignup localSignup;
    private IUserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.userRepository = mock(IUserRepository.class);
        this.localSignup = new LocalSignup(userRepository);
    }

    @Test
    @DisplayName("Should throw EmailAlreadyInUseException if email is already in use")
    void shouldThrowEmailAlreadyInUseExceptionIfEmailIsAlreadyInUse() {
        LocalSigninRequest request = AuthTestFixture.anyLocalSigninRequest();
        UserRef existingUser = AuthTestFixture.anyUserRef();

        when(this.userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        Throwable exception = catchThrowable(() -> this.localSignup.execute(request));

        assertThat(exception).isInstanceOf(EmailAlreadyInUseException.class);
        assertThat(exception.getMessage()).isEqualTo("The email " + request.getEmail() + " is already in use.");
        verify(this.userRepository, times(1)).findByEmail(request.getEmail());
    }

    @Test
    @DisplayName("Should save user ref, encode the password, save auth details")
    void shouldSaveUserRefAndEncodePasswordAndSaveAuthDetails() {
        LocalSigninRequest request = AuthTestFixture.anyLocalSigninRequest();
        UserRef existingUser = AuthTestFixture.anyUserRef();

        when(this.userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());


    }

}
