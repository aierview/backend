package com.aierview.backend.auth.domain.contact.token;

import com.aierview.backend.auth.domain.entity.UserRef;

public interface ITokenGenerator {
    String generate(UserRef userRef);
}