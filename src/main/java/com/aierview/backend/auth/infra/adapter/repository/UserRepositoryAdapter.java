package com.aierview.backend.auth.infra.adapter.repository;

import com.aierview.backend.auth.domain.contact.repository.IUserRepository;
import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.infra.mapper.UserMapper;
import com.aierview.backend.auth.infra.persistence.entity.UserJpaEntity;
import com.aierview.backend.auth.infra.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements IUserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<UserRef> findByEmail(String email) {
        Optional<UserJpaEntity> entity = this.userJpaRepository.findByEmail(email);
        if (entity.isEmpty()) return Optional.empty();
        UserRef userRef = this.userMapper.mapToEntity(entity.get());
        return Optional.of(userRef);
    }

    @Override
    public UserRef save(UserRef user) {
        UserJpaEntity entity = this.userMapper.mapToJpa(user);
        entity = this.userJpaRepository.save(entity);
        return this.userMapper.mapToEntity(entity);
    }
}
