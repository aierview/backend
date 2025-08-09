package com.aierview.backend.interview.domain.contract.repository;

import com.aierview.backend.interview.domain.entity.Question;

import java.util.List;

public interface IQuestionRepository {
    List<Question> saveAll(List<Question> questions);
}
