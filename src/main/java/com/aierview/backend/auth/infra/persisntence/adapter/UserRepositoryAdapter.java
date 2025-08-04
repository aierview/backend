package com.aierview.backend.auth.infra.persisntence.adapter;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.repository.IUserRepository;
import com.aierview.backend.auth.infra.mapper.UserMapper;
import com.aierview.backend.auth.infra.persisntence.jpa.entity.UserJpaEntity;
import com.aierview.backend.auth.infra.persisntence.jpa.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements IUserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<UserRef> findByEmail(String email) {
        Optional<UserJpaEntity> entity = this.userJpaRepository.findByEmail(email);
        if (entity.isEmpty()) return Optional.empty();
        UserRef userRef = this.userMapper.userJpaEntityToUserRef(entity.get());
        return Optional.of(userRef);
    }

    @Override
    public UserRef save(UserRef user) {
        UserJpaEntity entity = this.userMapper.userRefToUserJpaEntity(user);
        entity = this.userJpaRepository.save(entity);
        return this.userMapper.userJpaEntityToUserRef(entity);
    }
}
