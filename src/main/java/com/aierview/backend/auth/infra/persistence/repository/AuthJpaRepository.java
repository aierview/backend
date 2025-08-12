package com.aierview.backend.auth.infra.persistence.repository;

import com.aierview.backend.auth.infra.persistence.entity.AuthJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthJpaRepository extends JpaRepository<AuthJpaEntity, Long> {
    Optional<AuthJpaEntity> findByUserId(Long userId);
}
