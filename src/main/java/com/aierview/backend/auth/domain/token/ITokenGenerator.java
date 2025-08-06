package com.aierview.backend.auth.domain.token;

import com.aierview.backend.auth.domain.entity.UserRef;

public interface ITokenGenerator {
    String generate(UserRef userRef);
}