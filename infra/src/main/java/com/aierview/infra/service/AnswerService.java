package com.aierview.infra.service;

import com.aierview.application.gateway.answer.IReadAnswerByQuestionIdGateway;
import com.aierview.domain.entity.Answer;
import com.aierview.infra.api.mapper.AnswerMapper;
import com.aierview.infra.persistence.repository.AnswerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnswerService implements IReadAnswerByQuestionIdGateway {
    private final AnswerRepository repository;
    private final AnswerMapper answerMapper;

    @Override
    public Answer read(String id) {
        return repository.findFirstByQuestionId(id)
                .map(entity -> answerMapper.map(entity, Answer.class))
                .orElse(null);
    }

}
