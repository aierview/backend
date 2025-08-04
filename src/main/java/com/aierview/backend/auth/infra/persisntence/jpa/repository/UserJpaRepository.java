package com.aierview.backend.auth.infra.persisntence.jpa.repository;

import com.aierview.backend.auth.infra.persisntence.jpa.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    Optional<UserJpaEntity> findByEmail(String email);
}
