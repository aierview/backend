package com.aierview.backend.auth.domain.repository;

import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.domain.entity.UserRef;

import java.util.Optional;

public interface IAuthRepository {
    Auth save(Auth user);
}
