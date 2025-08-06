package com.aierview.backend.auth.infra.persisntence.adapter;

import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.domain.repository.IAuthRepository;
import com.aierview.backend.auth.infra.mapper.AuthMapper;
import com.aierview.backend.auth.infra.persisntence.jpa.entity.AuthJpaEntity;
import com.aierview.backend.auth.infra.persisntence.jpa.repository.AuthJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthRepositoryAdapter implements IAuthRepository {
    private final AuthMapper authMapper;
    private final AuthJpaRepository authRepository;

    @Override
    public Auth save(Auth auth) {
        AuthJpaEntity entity = this.authMapper.authToAuthJpaEntity(auth);
        entity = this.authRepository.save(entity);
        return this.authMapper.authJpaEntityToAuth(entity);
    }

    @Override
    public Optional<Auth> findByUserId(Long userId) {
        return Optional.empty();
    }
}
