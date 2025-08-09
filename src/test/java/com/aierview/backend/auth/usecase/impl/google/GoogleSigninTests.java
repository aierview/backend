package com.aierview.backend.auth.usecase.impl.google;

import com.aierview.backend.auth.domain.contact.google.IExtractUserDetails;
import com.aierview.backend.auth.domain.contact.repository.IAuthRepository;
import com.aierview.backend.auth.domain.contact.repository.IUserRepository;
import com.aierview.backend.auth.domain.contact.token.ITokenGenerator;
import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.enums.AuthProvider;
import com.aierview.backend.auth.domain.exceptions.InvalidCredentialException;
import com.aierview.backend.auth.domain.exceptions.InvalidGoogleIdTokenException;
import com.aierview.backend.auth.domain.model.cookie.CookieResponse;
import com.aierview.backend.auth.domain.model.google.GoogleAccountModel;
import com.aierview.backend.auth.domain.model.google.GoogleAuhRequest;
import com.aierview.backend.auth.usecase.contract.cookie.IGenerateCookieResponse;
import com.aierview.backend.auth.usecase.contract.google.IGoogleSignin;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public class GoogleSigninTests {
    private IGoogleSignin googleSignin;
    private  IExtractUserDetails extractUserDetails;
    private  IUserRepository userRepository;
    private  ITokenGenerator tokenGenerator;
    private  IGenerateCookieResponse generateCookieResponse;
    private IAuthRepository authRepository;

    @BeforeEach
    void setUp() {
        this.extractUserDetails =  mock(IExtractUserDetails.class);
        this.userRepository = mock(IUserRepository.class);
        this.tokenGenerator = mock(ITokenGenerator.class);
        this.generateCookieResponse = mock(IGenerateCookieResponse.class);
        this.authRepository = mock(IAuthRepository.class);
        this.googleSignin =  new GoogleSignin(extractUserDetails, userRepository,
                tokenGenerator, generateCookieResponse,authRepository);
    }

    @Test
    @DisplayName("Should throw InvalidGoogleIdTokenException when Google account is invalid")
    void shouldThrowInvalidGoogleIdTokenExceptionWhenGoogleAccountIsInvalid() {
        GoogleAuhRequest request = AuthTestFixture.anyGoogleAuthRequest();

        when(this.extractUserDetails.extractUserDetails(request)).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> this.googleSignin.execute(request));

        assertThat(exception).isInstanceOf(InvalidGoogleIdTokenException.class);
        assertThat(exception.getMessage()).isEqualTo("Invalid Google account, please provide a valid Google account.");
        verify(this.extractUserDetails, Mockito.times(1)).extractUserDetails(request);
    }

    @Test
    @DisplayName("Should throw InvalidCredentialException when user does not exists on find by email ")
    void shouldThrowInvalidCredentialExceptionWhenUserDoesNotExistsOnFindByEmail() {
        GoogleAuhRequest request = AuthTestFixture.anyGoogleAuthRequest();
        GoogleAccountModel accountModel = AuthTestFixture.anyGoogleAccountModel();

        when(this.extractUserDetails.extractUserDetails(request)).thenReturn(Optional.of(accountModel));
        when(this.userRepository.findByEmail(accountModel.email())).thenReturn(Optional.empty());

        Throwable exception = catchThrowable(() -> this.googleSignin.execute(request));

        assertThat(exception).isInstanceOf(InvalidCredentialException.class);
        assertThat(exception.getMessage()).isEqualTo("Email or password is incorrect!");
        verify(this.userRepository, times(1)).findByEmail(accountModel.email());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialException when auth provider is not google")
    void shouldThrowInvalidCredentialExceptionWhenAuthProviderIsNotGoogle() {
        GoogleAuhRequest request = AuthTestFixture.anyGoogleAuthRequest();
        GoogleAccountModel accountModel = AuthTestFixture.anyGoogleAccountModel();
        UserRef savedUser = AuthTestFixture.anySavedUserRef();
        Auth savedAuth = AuthTestFixture.anySavedAuth(savedUser);

        when(this.extractUserDetails.extractUserDetails(request)).thenReturn(Optional.of(accountModel));
        when(this.userRepository.findByEmail(accountModel.email())).thenReturn(Optional.of(savedUser));
        when(this.authRepository.findByUserId(savedAuth.getId())).thenReturn(Optional.of(savedAuth));

        Throwable exception = catchThrowable(() -> this.googleSignin.execute(request));

        assertThat(exception).isInstanceOf(InvalidCredentialException.class);
        assertThat(exception.getMessage()).isEqualTo("Email or password is incorrect!");
        verify(this.userRepository, times(1)).findByEmail(accountModel.email());
        verify(this.authRepository, times(1)).findByUserId(savedAuth.getId());
    }

    @Test
    @DisplayName("Should return a CookieResponse with prod config when authentication succeeds and if the environment is prod")
    void shouldReturnACookieResponseWithProdConfigWhenAuthenticationSucceedsAndIfTheEnvironmentIsProd() {
        GoogleAuhRequest request = AuthTestFixture.anyGoogleAuthRequest();
        GoogleAccountModel accountModel = AuthTestFixture.anyGoogleAccountModel();
        UserRef savedUser = AuthTestFixture.anySavedUserRef();
        Auth savedAuth = AuthTestFixture.anySavedAuth(savedUser);
        savedAuth.setProvider(AuthProvider.GOOGLE);

        String token = UUID.randomUUID().toString();
        CookieResponse cookieResponse = AuthTestFixture.anyDevCookieResponse(token);

        when(this.extractUserDetails.extractUserDetails(request)).thenReturn(Optional.of(accountModel));
        when(this.userRepository.findByEmail(accountModel.email())).thenReturn(Optional.of(savedUser));
        when(this.authRepository.findByUserId(savedAuth.getId())).thenReturn(Optional.of(savedAuth));
        when(this.tokenGenerator.generate(savedUser)).thenReturn(token);
        when(this.generateCookieResponse.generate(cookieResponse.name(), cookieResponse.value())).thenReturn(cookieResponse);


        CookieResponse response =  this.googleSignin.execute(request);

        assertThat(response.name()).isEqualTo(cookieResponse.name());
        assertThat(response.value()).isEqualTo(cookieResponse.value());
        assertThat(response.httpOnly()).isEqualTo(cookieResponse.httpOnly());
        assertThat(response.secure()).isEqualTo(cookieResponse.secure());
        assertThat(response.sameSite()).isEqualTo(cookieResponse.sameSite());
        assertThat(response.path()).isEqualTo(cookieResponse.path());

        verify(this.userRepository, times(1)).findByEmail(accountModel.email());
        verify(this.tokenGenerator, times(1)).generate(savedUser);
        verify(this.generateCookieResponse, times(1)).generate(cookieResponse.name(), cookieResponse.value());
        verify(authRepository, times(1)).findByUserId(savedAuth.getId());
    }
}
