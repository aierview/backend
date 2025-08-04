package com.aierview.backend.auth.application.usecase.impl;

import com.aierview.backend.auth.application.usecase.contract.ILocalSignin;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.enums.UserRole;
import com.aierview.backend.auth.domain.exceptions.EmailAlreadyInUseException;
import com.aierview.backend.auth.domain.model.LocalSigninRequest;
import com.aierview.backend.auth.domain.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LocalSigninTests {
    private ILocalSignin localSignin;
    private IUserRepository userRepository;


    @BeforeEach
    void setUp() {
        this.userRepository = mock(IUserRepository.class);
        this.localSignin = new LocalSignin(userRepository);
    }

    @Test
    @DisplayName("Should throw EmailAlreadyInUseException if email is already in use")
    void shouldThrowEmailAlreadyInUseExceptionIfEmailIsAlreadyInUse() {
        LocalSigninRequest request = LocalSigninRequest.builder().email("any_email").password("any_password").build();
        UserRef existingUser =  UserRef
                .builder().id(1L)
                .name("any_name")
                .email("any_email")
                .role(UserRole.FULLSTACK)
                .build();

        when(this.userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        Throwable exception = catchThrowable(() -> this.localSignin.execute(request));

        assertThat(exception).isInstanceOf(EmailAlreadyInUseException.class);
        assertThat(exception.getMessage()).isEqualTo("The email " + request.getEmail() + " is already in use.");
        verify(this.userRepository, times(1)).findByEmail(request.getEmail());
    }

}
