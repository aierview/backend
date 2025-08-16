package com.aierview.backend.interview.infra.adapter.repository;

import com.aierview.backend.interview.domain.contract.repository.IQuestionRepository;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.infra.mapper.QuestionMapper;
import com.aierview.backend.interview.infra.persistence.entity.QuestionJpaEntity;
import com.aierview.backend.interview.infra.persistence.repository.QuestionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryAdapter implements IQuestionRepository {
    private final QuestionMapper questionMapper;
    private final QuestionJpaRepository questionJpaRepository;

    @Override
    public Question save(Question question) {
        QuestionJpaEntity entity = this.questionMapper.mapToJpa(question);
        entity = this.questionJpaRepository.save(entity);
        return this.questionMapper.mapToEntity(entity);
    }

    @Override
    public List<Question> saveAll(List<Question> questions) {
        List<QuestionJpaEntity> entities = this.questionMapper.mapToListJpa(questions);
        entities = this.questionJpaRepository.saveAll(entities);
        return this.questionMapper.mapToListEntity(entities);
    }

    @Override
    public Optional<Question> findById(Long id) {
        Optional<QuestionJpaEntity> jpaEntity = this.questionJpaRepository.findById(id);
        if (jpaEntity.isEmpty()) return Optional.empty();
        Question question = this.questionMapper.mapToEntity(jpaEntity.get());
        return Optional.of(question);
    }
}
