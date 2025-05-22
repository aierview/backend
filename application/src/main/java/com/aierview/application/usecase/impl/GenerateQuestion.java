package com.aierview.application.usecase.impl;

import com.aierview.application.gateway.IGenerateQuestionGateway;
import com.aierview.application.usecase.contract.IGenerateQuestion;
import com.aierview.application.usecase.contract.ISaveQuestions;
import com.aierview.domain.entity.GenerateQuestionParams;
import com.aierview.domain.entity.Question;
import com.aierview.domain.exceptions.UnexpectedException;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GenerateQuestion implements IGenerateQuestion {
    private final IGenerateQuestionGateway gateway;
    private final ISaveQuestions saveQuestions;

    @Override
    public List<Question> generate(GenerateQuestionParams params) throws UnexpectedException {
        List<Question> questions = gateway.generate(params);
        if (List.of(questions).isEmpty())
            throw new UnexpectedException("An error occurred, please contact the administrator!");
        return saveQuestions.save(questions);
    }
}
