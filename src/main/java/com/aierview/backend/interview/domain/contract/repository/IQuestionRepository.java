package com.aierview.backend.interview.domain.contract.repository;

import com.aierview.backend.interview.domain.entity.Question;

import java.util.List;
import java.util.Optional;

public interface IQuestionRepository {
    Question save(Question question);

    List<Question> saveAll(List<Question> questions);

    Optional<Question> findById(Long id);
}
