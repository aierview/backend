package com.aierview.infra.persistence.repository;

import com.aierview.infra.persistence.entity.QuestionareEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionareRepository extends MongoRepository<QuestionareEntity, String> {
}
