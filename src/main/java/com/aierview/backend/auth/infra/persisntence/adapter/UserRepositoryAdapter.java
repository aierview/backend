package com.aierview.backend.auth.infra.persisntence.adapter;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.repository.IUserRepository;
import com.aierview.backend.auth.infra.persisntence.jpa.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements IUserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<UserRef> findByEmail(String email) {
        this.userJpaRepository.findByEmail(email);
        return Optional.empty();
    }

    @Override
    public UserRef save(UserRef user) {
        return null;
    }
}
