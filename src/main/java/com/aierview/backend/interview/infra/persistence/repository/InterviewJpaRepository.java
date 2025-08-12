package com.aierview.backend.interview.infra.persistence.repository;

import com.aierview.backend.interview.infra.persistence.entity.InterviewJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewJpaRepository extends JpaRepository<InterviewJpaEntity, Long> {

}
