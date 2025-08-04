package com.aierview.backend.shared.testdata;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.enums.UserRole;
import com.aierview.backend.auth.domain.model.LocalSigninRequest;

public class AuthTestFixture {
    public static LocalSigninRequest anyLocalSigninRequest() {
        return LocalSigninRequest.builder()
                .email("any_email")
                .password("any_password")
                .build();
    }

    public static UserRef anyUserRef() {
        return UserRef.builder()
                .id(1L)
                .name("any_name")
                .email("any_email")
                .role(UserRole.FULLSTACK)
                .build();
    }
}
