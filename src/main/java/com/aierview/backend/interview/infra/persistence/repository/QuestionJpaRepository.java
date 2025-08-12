package com.aierview.backend.interview.infra.persistence.repository;

import com.aierview.backend.interview.infra.persistence.entity.QuestionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionJpaRepository extends JpaRepository<QuestionJpaEntity, Long> {

}
