package com.aierview.backend.auth.domain.repository;

import com.aierview.backend.auth.domain.entity.UserRef;

import java.util.Optional;

public interface IUserRepository {
    Optional<UserRef> findByEmail(String email);
}
