package com.aierview.backend.interview.infra.mapper;


import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.infra.persistence.entity.QuestionJpaEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper()
public interface QuestionMapper {
    List<QuestionJpaEntity> mapToJpa(List<Question> questions);

    List<Question> mapToEntity(List<QuestionJpaEntity> questionJpaEntities);
}