package com.aierview.backend.interview.infra.persistence.repository;

import com.aierview.backend.interview.infra.persistence.entity.QuestionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<QuestionJpaEntity, Long> {

}
