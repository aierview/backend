package com.aierview.backend.auth.domain.repository;

import com.aierview.backend.auth.domain.entity.Auth;

import java.util.Optional;

public interface IAuthRepository {
    Auth save(Auth auth);

    Optional<Auth> findByUserId(Long userId);
}
