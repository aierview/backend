package com.aierview.backend.shared.testdata;

import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.enums.AuthProvider;
import com.aierview.backend.auth.domain.enums.UserRole;
import com.aierview.backend.auth.domain.model.cookie.CookieResponse;
import com.aierview.backend.auth.domain.model.google.GoogleAccountModel;
import com.aierview.backend.auth.domain.model.google.GoogleAuhRequest;
import com.aierview.backend.auth.domain.model.local.LocalSigninRequest;
import com.aierview.backend.auth.domain.model.local.LocalSignupRequest;
import com.aierview.backend.auth.infra.persistence.entity.AuthJpaEntity;
import com.aierview.backend.auth.infra.persistence.entity.UserJpaEntity;

import java.util.UUID;

public class AuthTestFixture {

    public static LocalSignupRequest anyLocalSignupRequest() {
        return LocalSignupRequest.builder()
                .email("example@example.com")
                .password("Password123!")
                .name("john Snow Smith")
                .role(UserRole.FULLSTACK)
                .build();
    }

    public static UserRef anyUserRef() {
        return UserRef.builder()
                .name("John Snow Smith")
                .email("example@example.com")
                .role(UserRole.FULLSTACK)
                .build();
    }

    public static UserRef anySavedUserRef() {
        return UserRef.builder()
                .id(1L)
                .name("john Snow Smith")
                .email("example@example.com")
                .role(UserRole.FULLSTACK)
                .build();
    }

    public static Auth anyAuth() {

        return Auth
                .builder()
                .password(UUID.randomUUID().toString())
                .provider(AuthProvider.LOCAL)
                .user(anySavedUserRef())
                .build();
    }


    public static Auth anyAuth(UserRef savedUser) {
        return Auth
                .builder()
                .password(UUID.randomUUID().toString())
                .provider(AuthProvider.LOCAL)
                .user(savedUser)
                .build();
    }

    public static Auth anyGoogleAuth(UserRef savedUser, GoogleAccountModel googleAccountModel) {
        return Auth
                .builder()
                .provider(AuthProvider.GOOGLE)
                .user(savedUser)
                .picture(googleAccountModel.picture())
                .build();
    }


    public static Auth anySavedAuth(UserRef savedUser) {
        return Auth
                .builder()
                .id(1L)
                .password(UUID.randomUUID().toString())
                .provider(AuthProvider.LOCAL)
                .user(savedUser)
                .build();
    }

    public static Auth anyGoogleSavedAuth(UserRef savedUser, GoogleAccountModel accountModel) {
        return Auth
                .builder()
                .id(1L)
                .provider(AuthProvider.GOOGLE)
                .user(savedUser)
                .picture(accountModel.picture())
                .build();
    }

    public static Auth anySavedAuth(AuthJpaEntity savedAuth) {
        UserRef user = UserRef
                .builder()
                .id(savedAuth.getUser().getId())
                .name(savedAuth.getUser().getName())
                .email(savedAuth.getUser().getEmail())
                .role(savedAuth.getUser().getRole())
                .build();
        return Auth
                .builder()
                .id(savedAuth.getId())
                .password(savedAuth.getPassword())
                .provider(savedAuth.getProvider())
                .user(user)
                .build();
    }

    public static AuthJpaEntity anyAuthJpaEntity(Auth auth) {
        UserJpaEntity userJpaEntity = UserJpaEntity
                .builder()
                .id(auth.getUser().getId())
                .name(auth.getUser().getName())
                .email(auth.getUser().getEmail())
                .role(auth.getUser().getRole())
                .build();

        return AuthJpaEntity
                .builder()
                .password(auth.getPassword())
                .provider(auth.getProvider())
                .user(userJpaEntity)
                .build();
    }

    public static AuthJpaEntity anyAuthJpaEntity(UserJpaEntity userJpaEntity, String hashedPassword) {
        return AuthJpaEntity
                .builder()
                .password(hashedPassword)
                .provider(AuthProvider.LOCAL)
                .user(userJpaEntity)
                .build();
    }

    public static AuthJpaEntity anySavedAuthJpaEntity(Auth auth) {
        UserJpaEntity userJpaEntity = UserJpaEntity
                .builder()
                .id(auth.getUser().getId())
                .name(auth.getUser().getName())
                .email(auth.getUser().getEmail())
                .role(auth.getUser().getRole())
                .build();

        return AuthJpaEntity
                .builder()
                .id(1L)
                .password(auth.getPassword())
                .provider(auth.getProvider())
                .user(userJpaEntity)
                .build();
    }

    public static UserJpaEntity anySavedUserJpaEntity(String email) {
        AuthJpaEntity authJpaEntity = AuthTestFixture.anyAuthJpaEntity(anySavedAuth(anySavedUserRef()));
        authJpaEntity.getUser().setEmail(email);
        UserJpaEntity userJpaEntity = UserJpaEntity.builder()
                .id(1L)
                .name("john Snow Smith")
                .email(email)
                .role(UserRole.FULLSTACK)
                .build();

        authJpaEntity.setUser(userJpaEntity);
        userJpaEntity.setAuth(authJpaEntity);
        return userJpaEntity;
    }

    public static UserJpaEntity anyUserJpaEntity() {
        return UserJpaEntity.builder()
                .name("john Snow Smith")
                .email("example@example.com")
                .role(UserRole.FULLSTACK)
                .build();
    }

    public static UserRef anyUserRef(UserJpaEntity userJpaEntity) {
        return UserRef
                .builder()
                .id(userJpaEntity.getId())
                .name(userJpaEntity.getName())
                .email(userJpaEntity.getEmail())
                .role(userJpaEntity.getRole())
                .build();
    }

    public static UserJpaEntity anyUserJpaEntity(UserRef userRef) {
        return UserJpaEntity
                .builder()
                .id(userRef.getId())
                .name(userRef.getName())
                .email(userRef.getEmail())
                .role(userRef.getRole())
                .build();
    }

    public static UserJpaEntity anyUserJpaEntity(UserJpaEntity userJpaEntity) {
        return UserJpaEntity
                .builder()
                .id(1L)
                .name(userJpaEntity.getName())
                .email(userJpaEntity.getEmail())
                .role(userJpaEntity.getRole())
                .build();
    }


    public static LocalSigninRequest anyLocalSigninRequest() {
        return LocalSigninRequest
                .builder()
                .email("example@example.com")
                .password("Password123!")
                .build();
    }

    public static CookieResponse anyProdCookieResponse(String value) {
        return new CookieResponse("token", value, true, true, "NONE", "/");
    }

    public static CookieResponse anyHomologCookieResponse(String value) {
        return new CookieResponse("token", value, true, true, "NONE", "/");
    }

    public static CookieResponse anyDevCookieResponse(String value) {
        return new CookieResponse("token", value, true, false, "LAX", "/");
    }

    public static GoogleAccountModel anyGoogleAccountModel() {
        return new GoogleAccountModel("John Snow Smith", "example@example.com", "any_pic");
    }

    public static UserRef anyUserRef(GoogleAccountModel googleAccountModel) {
        return UserRef.builder()
                .name(googleAccountModel.name())
                .email(googleAccountModel.email())
                .build();
    }

    public static UserRef anySavedUserRef(GoogleAccountModel googleAccountModel) {
        return UserRef.builder()
                .id(1L)
                .name(googleAccountModel.name())
                .email(googleAccountModel.email())
                .build();
    }

    public static GoogleAuhRequest anyGoogleAuthRequest() {
        return new GoogleAuhRequest("any_valid_token");
    }

    public static UserRef anySavedUser(UserJpaEntity savedUserJpaEntity) {
        return UserRef
                .builder()
                .id(savedUserJpaEntity.getId())
                .name(savedUserJpaEntity.getName())
                .email(savedUserJpaEntity.getEmail())
                .role(savedUserJpaEntity.getRole())
                .build();

    }
}
