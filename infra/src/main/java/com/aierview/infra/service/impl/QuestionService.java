package com.aierview.infra.service.impl;

import com.aierview.application.gateway.IGenerateQuestionGateway;
import com.aierview.application.gateway.ISaveQuestionsGateway;
import com.aierview.domain.entity.GenerateQuestionParams;
import com.aierview.domain.entity.Question;
import com.aierview.infra.api.mapper.QuestionMapper;
import com.aierview.infra.persistence.entity.QuestionEntity;
import com.aierview.infra.persistence.repository.QuestionRepository;
import com.aierview.infra.service.contract.IGeminiService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuestionService implements ISaveQuestionsGateway, IGenerateQuestionGateway {
    private final IGeminiService service;
    private final QuestionRepository repository;
    private final QuestionMapper mapper;

    @Override
    public List<Question> save(List<Question> questions) {
        List<QuestionEntity> entities = mapper.toListQuestionEntity(questions);
        entities = repository.saveAll(entities);
        return mapper.toListQuestion(entities);
    }

    @Override
    public List<Question> generate(GenerateQuestionParams params) {
        List<String> questions = service.getGeminiResponse(params);
        if (questions == null || questions.isEmpty()) return Collections.emptyList();

        return questions.stream()
                .filter(statement -> statement != null && !statement.trim().isEmpty())
                .map(statement -> Question.builder()
                        .framework(params.getFramework())
                        .language(params.getLanguage())
                        .specialty(params.getSpecialty())
                        .statement(statement)
                        .level(params.getLevel())
                        .build())
                .collect(Collectors.toList());

    }
}
