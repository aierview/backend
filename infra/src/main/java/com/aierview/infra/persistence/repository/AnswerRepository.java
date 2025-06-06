package com.aierview.infra.persistence.repository;

import com.aierview.infra.persistence.entity.AnswerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AnswerRepository extends MongoRepository<AnswerEntity, String> {
    Optional<AnswerEntity> findFirstByQuestionId(String questionId);
}
