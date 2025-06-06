package com.aierview.infra.api.mapper;

import com.aierview.domain.entity.Answer;
import com.aierview.domain.entity.Question;
import com.aierview.infra.persistence.entity.AnswerEntity;
import com.aierview.infra.persistence.entity.QuestionEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionMapper extends ModelMapper {
    public List<QuestionEntity> toListQuestionEntity(List<Question> questions) {
        return questions.stream().map(this::toQuestionEntity).toList();
    }

    public QuestionEntity toQuestionEntity(Question question) {
        return QuestionEntity
                .builder()
                .id(question.getId())
                .framework(question.getFramework())
                .language(question.getLanguage())
                .specialty(question.getSpecialty())
                .statement(question.getStatement())
                .level(question.getLevel())
                .build();
    }

    public List<Question> toListQuestion(List<QuestionEntity> entities) {
        return entities.stream().map(this::toQuestion).toList();
    }

    public Question toQuestion(QuestionEntity entity) {
        return Question
                .builder()
                .id(entity.getId())
                .framework(entity.getFramework())
                .language(entity.getLanguage())
                .specialty(entity.getSpecialty())
                .statement(entity.getStatement())
                .level(entity.getLevel())
                .build();
    }

    public AnswerEntity toAnswerEntity(Answer answer) {
        return AnswerEntity
                .builder()
                .id(answer.getId())
                .questionId(answer.getQuestionId())
                .answerText(answer.getAnswerText())
                .feedback(answer.getFeedback())
                .build();
    }

    public Answer toAnswer(AnswerEntity entity) {
        return Answer
                .builder()
                .id(entity.getId())
                .questionId(entity.getQuestionId())
                .answerText(entity.getAnswerText())
                .feedback(entity.getFeedback())
                .build();
    }

}
