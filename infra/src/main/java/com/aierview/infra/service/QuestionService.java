package com.aierview.infra.service;

import com.aierview.application.gateway.answer.IUpdateAnswerGateway;
import com.aierview.application.gateway.question.IAnswerQuestionGateway;
import com.aierview.application.gateway.question.IReadQuestionByIdGateway;
import com.aierview.application.gateway.question.ISaveQuestionsGateway;
import com.aierview.domain.entity.Answer;
import com.aierview.domain.entity.Question;
import com.aierview.infra.api.mapper.QuestionMapper;
import com.aierview.infra.persistence.entity.AnswerEntity;
import com.aierview.infra.persistence.entity.QuestionEntity;
import com.aierview.infra.persistence.repository.AnswerRepository;
import com.aierview.infra.persistence.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QuestionService implements IAnswerQuestionGateway, IReadQuestionByIdGateway, ISaveQuestionsGateway, IUpdateAnswerGateway {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionMapper mapper;

    @Override
    public Answer answer(Answer answer) {
        AnswerEntity entity = mapper.toAnswerEntity(answer);
        entity = answerRepository.save(entity);
        return mapper.toAnswer(entity);
    }

    @Override
    public Question read(String id) {
        Optional<QuestionEntity> entity = questionRepository.findById(id);
        if (entity.isEmpty()) return null;
        return mapper.toQuestion(entity.get());
    }

    @Override
    public List<Question> save(List<Question> questions) {
        List<QuestionEntity> entities = mapper.toListQuestionEntity(questions);
        entities = questionRepository.saveAll(entities);
        return mapper.toListQuestion(entities);
    }

    @Override
    public void update(Answer answer) {
        AnswerEntity entity = mapper.toAnswerEntity(answer);
        answerRepository.save(entity);
    }
}
