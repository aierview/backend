package com.aierview.infra.persistence.repository;

import com.aierview.infra.persistence.entity.QuestionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface QuestionRepository extends MongoRepository<QuestionEntity, UUID> {
}
