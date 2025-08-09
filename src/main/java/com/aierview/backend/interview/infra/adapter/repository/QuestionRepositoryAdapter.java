package com.aierview.backend.interview.infra.adapter.repository;

import com.aierview.backend.interview.domain.contract.repository.IQuestionRepository;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.infra.mapper.QuestionMapper;
import com.aierview.backend.interview.infra.persistence.entity.QuestionJpaEntity;
import com.aierview.backend.interview.infra.persistence.repository.QuestionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryAdapter implements IQuestionRepository {
    private final QuestionMapper questionMapper;
    private final QuestionJpaRepository questionJpaRepository;


    @Override
    public List<Question> saveAll(List<Question> questions) {
        List<QuestionJpaEntity> entities = this.questionMapper.mapToJpa(questions);
        entities = this.questionJpaRepository.saveAll(entities);
        return this.questionMapper.mapToEntity(entities);
    }
}
