package com.aierview.backend.auth.usecase.impl;

import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.exceptions.InvalidCredentialException;
import com.aierview.backend.auth.domain.model.CookieResponse;
import com.aierview.backend.auth.domain.model.LocalSigninRequest;
import com.aierview.backend.auth.domain.repository.IAuthRepository;
import com.aierview.backend.auth.domain.repository.IUserRepository;
import com.aierview.backend.auth.domain.security.IPasswordComparer;
import com.aierview.backend.auth.domain.token.ITokenGenerator;
import com.aierview.backend.auth.usecase.contract.IGenerateCookieResponse;
import com.aierview.backend.auth.usecase.contract.ILocalSignin;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public class LocalSigninTests {
    private ILocalSignin localSignin;
    private IUserRepository userRepository;
    private IAuthRepository authRepository;
    private IPasswordComparer passwordComparer;
    private ITokenGenerator tokenGenerator;
    private IGenerateCookieResponse generateCookieResponse;

    @BeforeEach
    void setUp() {
        this.userRepository = mock(IUserRepository.class);
        this.authRepository = mock(IAuthRepository.class);
        this.passwordComparer = mock(IPasswordComparer.class);
        this.tokenGenerator = mock(ITokenGenerator.class);
        this.generateCookieResponse = mock(IGenerateCookieResponse.class);
        this.localSignin = new LocalSignin(this.userRepository,
                authRepository, passwordComparer, tokenGenerator, generateCookieResponse);
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

    @Test
    @DisplayName("Should return a CookieResponse with prod config when authentication succeeds and if the environment is prod")
    void shouldReturnACookieResponseWithProdConfigWhenAuthenticationSucceedsAndIfTheEnvironmentIsProd() {
        LocalSigninRequest request = AuthTestFixture.anyLocalSigninRequest();
        UserRef savedUser = AuthTestFixture.anySavedUserRef();
        Auth savedAuth = AuthTestFixture.anySavedAuth(savedUser);

        String token = UUID.randomUUID().toString();
        CookieResponse cookieResponse = AuthTestFixture.anyProdCookieResponse(token);

        when(this.userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(savedUser));
        when(this.authRepository.findByUserId(savedUser.getId())).thenReturn(Optional.of(savedAuth));
        when(this.passwordComparer.matches(request.getPassword(), savedAuth.getPassword())).thenReturn(true);
        when(this.tokenGenerator.generate(savedUser)).thenReturn(token);
        when(this.generateCookieResponse.generate(cookieResponse.name(), cookieResponse.value())).thenReturn(cookieResponse);

        CookieResponse response = this.localSignin.execute(request);


        assertThat(response.name()).isEqualTo(cookieResponse.name());
        assertThat(response.value()).isEqualTo(cookieResponse.value());
        assertThat(response.httpOnly()).isEqualTo(cookieResponse.httpOnly());
        assertThat(response.secure()).isEqualTo(cookieResponse.secure());
        assertThat(response.sameSite()).isEqualTo(cookieResponse.sameSite());
        assertThat(response.path()).isEqualTo(cookieResponse.path());

        verify(this.userRepository, times(1)).findByEmail(request.getEmail());
        verify(this.authRepository, times(1)).findByUserId(savedUser.getId());
        verify(this.passwordComparer, times(1)).matches(request.getPassword(), savedAuth.getPassword());
        verify(this.tokenGenerator, times(1)).generate(savedUser);
        verify(this.generateCookieResponse, times(1)).generate(cookieResponse.name(), cookieResponse.value());
    }

    @Test
    @DisplayName("Should return a CookieResponse with homolog config when authentication succeeds and if the environment is homolog")
    void shouldReturnACookieResponseWithHomologConfigWhenAuthenticationSucceedsAndIfTheEnvironmentIsHomolog() {
        LocalSigninRequest request = AuthTestFixture.anyLocalSigninRequest();
        UserRef savedUser = AuthTestFixture.anySavedUserRef();
        Auth savedAuth = AuthTestFixture.anySavedAuth(savedUser);

        String token = UUID.randomUUID().toString();
        CookieResponse cookieResponse = AuthTestFixture.anyHomologCookieResponse(token);

        when(this.userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(savedUser));
        when(this.authRepository.findByUserId(savedUser.getId())).thenReturn(Optional.of(savedAuth));
        when(this.passwordComparer.matches(request.getPassword(), savedAuth.getPassword())).thenReturn(true);
        when(this.tokenGenerator.generate(savedUser)).thenReturn(token);
        when(this.generateCookieResponse.generate(cookieResponse.name(), cookieResponse.value())).thenReturn(cookieResponse);

        CookieResponse response = this.localSignin.execute(request);


        assertThat(response.name()).isEqualTo(cookieResponse.name());
        assertThat(response.value()).isEqualTo(cookieResponse.value());
        assertThat(response.httpOnly()).isEqualTo(cookieResponse.httpOnly());
        assertThat(response.secure()).isEqualTo(cookieResponse.secure());
        assertThat(response.sameSite()).isEqualTo(cookieResponse.sameSite());
        assertThat(response.path()).isEqualTo(cookieResponse.path());

        verify(this.userRepository, times(1)).findByEmail(request.getEmail());
        verify(this.authRepository, times(1)).findByUserId(savedUser.getId());
        verify(this.passwordComparer, times(1)).matches(request.getPassword(), savedAuth.getPassword());
        verify(this.tokenGenerator, times(1)).generate(savedUser);
        verify(this.generateCookieResponse, times(1)).generate(cookieResponse.name(), cookieResponse.value());
    }

    @Test
    @DisplayName("Should return a CookieResponse with dev config when authentication succeeds and if the environment is dev")
    void shouldReturnACookieResponseWithDevConfigWhenAuthenticationSucceedsAndIfTheEnvironmentIsDev() {
        LocalSigninRequest request = AuthTestFixture.anyLocalSigninRequest();
        UserRef savedUser = AuthTestFixture.anySavedUserRef();
        Auth savedAuth = AuthTestFixture.anySavedAuth(savedUser);

        String token = UUID.randomUUID().toString();
        CookieResponse cookieResponse = AuthTestFixture.anyDevCookieResponse(token);

        when(this.userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(savedUser));
        when(this.authRepository.findByUserId(savedUser.getId())).thenReturn(Optional.of(savedAuth));
        when(this.passwordComparer.matches(request.getPassword(), savedAuth.getPassword())).thenReturn(true);
        when(this.tokenGenerator.generate(savedUser)).thenReturn(token);
        when(this.generateCookieResponse.generate(cookieResponse.name(), cookieResponse.value())).thenReturn(cookieResponse);

        CookieResponse response = this.localSignin.execute(request);


        assertThat(response.name()).isEqualTo(cookieResponse.name());
        assertThat(response.value()).isEqualTo(cookieResponse.value());
        assertThat(response.httpOnly()).isEqualTo(cookieResponse.httpOnly());
        assertThat(response.secure()).isEqualTo(cookieResponse.secure());
        assertThat(response.sameSite()).isEqualTo(cookieResponse.sameSite());
        assertThat(response.path()).isEqualTo(cookieResponse.path());

        verify(this.userRepository, times(1)).findByEmail(request.getEmail());
        verify(this.authRepository, times(1)).findByUserId(savedUser.getId());
        verify(this.passwordComparer, times(1)).matches(request.getPassword(), savedAuth.getPassword());
        verify(this.tokenGenerator, times(1)).generate(savedUser);
        verify(this.generateCookieResponse, times(1)).generate(cookieResponse.name(), cookieResponse.value());
    }
}
