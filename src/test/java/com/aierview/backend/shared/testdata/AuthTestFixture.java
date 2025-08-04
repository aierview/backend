package com.aierview.backend.shared.testdata;

import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.enums.AuthProvider;
import com.aierview.backend.auth.domain.enums.UserRole;
import com.aierview.backend.auth.domain.model.LocalSigninRequest;

import java.util.UUID;

public class AuthTestFixture {

    public static LocalSigninRequest anyLocalSigninRequest() {
        return LocalSigninRequest.builder()
                .email("any_email")
                .password("any_password")
                .name("any_name")
                .role(UserRole.FULLSTACK)
                .build();
    }

    public static UserRef anyUserRef() {
        return UserRef.builder()
                .name("any_name")
                .email("any_email")
                .role(UserRole.FULLSTACK)
                .build();
    }

    public static UserRef anySavedUserRef() {
        return UserRef.builder()
                .id(1L)
                .name("any_name")
                .email("any_email")
                .role(UserRole.FULLSTACK)
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

    public static Auth anySavedAuth(UserRef savedUser) {
        return Auth
                .builder()
                .id(1L)
                .password(UUID.randomUUID().toString())
                .provider(AuthProvider.LOCAL)
                .user(savedUser)
                .build();
    }
}
