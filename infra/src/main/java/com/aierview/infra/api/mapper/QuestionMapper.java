package com.aierview.infra.api.mapper;

import com.aierview.domain.entity.Question;
import com.aierview.infra.persistence.entity.QuestionEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionMapper extends ModelMapper {
    public List<QuestionEntity> toListQuestionEntity(List<Question> questions) {
        return questions.stream().map(question -> this.map(question, QuestionEntity.class)).toList();
    }

    public List<Question> toListQuestion(List<QuestionEntity> entities) {
        return entities.stream().map(question -> this.map(entities, Question.class)).toList();
    }
}
