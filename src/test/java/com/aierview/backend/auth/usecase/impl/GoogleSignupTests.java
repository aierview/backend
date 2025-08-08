package com.aierview.backend.auth.usecase.impl;

import com.aierview.backend.auth.domain.contact.google.IExtractUserDetails;
import com.aierview.backend.auth.domain.contact.repository.IAuthRepository;
import com.aierview.backend.auth.domain.contact.repository.IUserRepository;
import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.exceptions.EmailAlreadyInUseException;
import com.aierview.backend.auth.domain.exceptions.InvalidGoogleIdTokenException;
import com.aierview.backend.auth.domain.model.GoogleAccountModel;
import com.aierview.backend.auth.usecase.contract.IGoogleSignup;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GoogleSignupTests {
    private IGoogleSignup googleSignup;
    private IExtractUserDetails extractUserDetails;
    private IUserRepository userRepository;
    private IAuthRepository authRepository;

    @BeforeEach
    void setup() {
        this.extractUserDetails = Mockito.mock(IExtractUserDetails.class);
        this.userRepository = Mockito.mock(IUserRepository.class);
        this.authRepository = Mockito.mock(IAuthRepository.class);
        this.googleSignup = new GoogleSignup(extractUserDetails, userRepository, authRepository);
    }

    @Test
    @DisplayName("Should throw InvalidGoogleIdTokenException when Google account is invalid")
    void shouldThrowInvalidGoogleIdTokenExceptionWhenGoogleAccountIsInvalid() {
        String idToken = "any_token";

        when(this.extractUserDetails.extract(idToken)).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> this.googleSignup.execute(idToken));

        assertThat(exception).isInstanceOf(InvalidGoogleIdTokenException.class);
        assertThat(exception.getMessage()).isEqualTo("Invalid Google account, please provide a valid Google account.");
        verify(this.extractUserDetails, Mockito.times(1)).extract(idToken);
    }

    @Test
    @DisplayName("Should throw EmailAlreadyInUseException when user already exists on google signup")
    void shouldThrowEmailAlreadyInUseExceptionWhenUserAlreadyExistsOnGoogleSignup() {
        String idToken = "any_token";
        GoogleAccountModel accountModel = AuthTestFixture.anyGoogleAccountModel();
        UserRef savedUser = AuthTestFixture.anySavedUserRef(accountModel);

        when(this.extractUserDetails.extract(idToken)).thenReturn(Optional.of(accountModel));
        when(this.userRepository.findByEmail(accountModel.email())).thenReturn(Optional.of(savedUser));

        Throwable exception = Assertions.catchThrowable(() -> this.googleSignup.execute(idToken));

        assertThat(exception).isInstanceOf(EmailAlreadyInUseException.class);
        assertThat(exception.getMessage()).isEqualTo("The email " + accountModel.email() + " is already in use.");
        verify(this.extractUserDetails, Mockito.times(1)).extract(idToken);
        verify(this.userRepository, Mockito.times(1)).findByEmail(accountModel.email());
    }

    @Test
    @DisplayName("Should save user and account details on signup success")
    void shouldSaveUserAndAccountDetailsOnSignupSuccess() {
        String idToken = "any_token";
        GoogleAccountModel accountModel = AuthTestFixture.anyGoogleAccountModel();

        UserRef toSaveUser = AuthTestFixture.anyUserRef(accountModel);
        UserRef savedUser = AuthTestFixture.anySavedUserRef(accountModel);

        Auth toSaveAuth = AuthTestFixture.anyGoogleAuth(savedUser, accountModel);
        Auth savedAuth = AuthTestFixture.anyGoogleSavedAuth(savedUser, accountModel);

        when(this.extractUserDetails.extract(idToken)).thenReturn(Optional.of(accountModel));
        when(this.userRepository.findByEmail(accountModel.email())).thenReturn(Optional.empty());
        when(this.userRepository.save(toSaveUser)).thenReturn(savedUser);
        when(this.authRepository.save(toSaveAuth)).thenReturn(savedAuth);

        this.googleSignup.execute(idToken);

        verify(this.extractUserDetails, Mockito.times(1)).extract(idToken);
        verify(this.userRepository, Mockito.times(1)).findByEmail(accountModel.email());
        verify(this.userRepository, Mockito.times(1)).save(toSaveUser);
        verify(this.authRepository, Mockito.times(1)).save(toSaveAuth);
    }
}
