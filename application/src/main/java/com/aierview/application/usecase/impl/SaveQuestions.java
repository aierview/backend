package com.aierview.application.usecase.impl;

import com.aierview.application.gateway.ISaveQuestionsGateway;
import com.aierview.application.usecase.contract.ISaveQuestions;
import com.aierview.domain.entity.Question;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SaveQuestions implements ISaveQuestions {
    private final ISaveQuestionsGateway gateway;

    @Override
    public List<Question> save(List<Question> questions) {
        return gateway.save(questions);
    }
}
