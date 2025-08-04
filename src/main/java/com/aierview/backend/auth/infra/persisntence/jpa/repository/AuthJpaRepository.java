package com.aierview.backend.auth.infra.persisntence.jpa.repository;

import com.aierview.backend.auth.infra.persisntence.jpa.entity.AuthJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthJpaRepository extends JpaRepository<AuthJpaEntity, Long> {
}
