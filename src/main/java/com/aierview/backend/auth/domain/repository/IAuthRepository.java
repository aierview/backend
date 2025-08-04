package com.aierview.backend.auth.domain.repository;

import com.aierview.backend.auth.domain.entity.Auth;

public interface IAuthRepository {
    Auth save(Auth auth);
}
