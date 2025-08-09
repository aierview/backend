package com.aierview.backend.auth.infra.adapter.repository;

import com.aierview.backend.auth.domain.contact.repository.IAuthRepository;
import com.aierview.backend.auth.domain.entity.Auth;
import com.aierview.backend.auth.infra.mapper.AuthMapper;
import com.aierview.backend.auth.infra.persisntence.entity.AuthJpaEntity;
import com.aierview.backend.auth.infra.persisntence.repository.AuthJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
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
        Optional<AuthJpaEntity> entity = this.authRepository.findByUserId(userId);
        return entity.map(this.authMapper::authJpaEntityToAuth);
    }
}
