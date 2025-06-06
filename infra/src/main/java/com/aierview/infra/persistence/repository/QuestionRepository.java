package com.aierview.infra.persistence.repository;

import com.aierview.infra.persistence.entity.QuestionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<QuestionEntity, String> {
}
