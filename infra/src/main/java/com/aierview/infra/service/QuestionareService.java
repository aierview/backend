package com.aierview.infra.service;

import com.aierview.application.gateway.questionnaire.IGenerateQuestionnaireGateway;
import com.aierview.application.usecase.contract.questionnaire.IReadQuestionareById;
import com.aierview.domain.entity.Questionnaire;
import com.aierview.infra.api.mapper.QuestionareMapper;
import com.aierview.infra.persistence.entity.QuestionareEntity;
import com.aierview.infra.persistence.repository.QuestionareRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class QuestionareService implements IGenerateQuestionnaireGateway, IReadQuestionareById {
    private final QuestionareRepository repository;
    private final QuestionareMapper mapper;

    @Override
    public Questionnaire generate(Questionnaire questionnaire) {
        QuestionareEntity entity = mapper.map(questionnaire, QuestionareEntity.class);
        repository.save(entity);
        return mapper.map(entity, Questionnaire.class);
    }

    @Override
    public Questionnaire read(String questionareId) {
        Optional<QuestionareEntity> entity = repository.findById(questionareId);
        if (entity.isEmpty()) return null;
        return mapper.map(entity.get(), Questionnaire.class);
    }
}
