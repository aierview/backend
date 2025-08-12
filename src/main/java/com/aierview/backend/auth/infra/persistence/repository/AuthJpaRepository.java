package com.aierview.backend.auth.infra.persistence.repository;

import com.aierview.backend.auth.infra.persistence.entity.AuthJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthJpaRepository extends JpaRepository<AuthJpaEntity, Long> {
    Optional<AuthJpaEntity> findByUserId(Long userId);
}
