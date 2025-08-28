package com.aierview.backend.interview.infra.mapper;


import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.infra.persistence.entity.QuestionJpaEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    Question mapToEntity(QuestionJpaEntity questionJpaEntity);

    QuestionJpaEntity mapToJpa(Question question);

    List<QuestionJpaEntity> mapToListJpa(List<Question> questions);

    List<Question> mapToListEntity(List<QuestionJpaEntity> questionJpaEntities);
}