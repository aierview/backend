package com.aierview.application.usecase.impl.AI;

import com.aierview.application.gateway.AI.IGenerateQuestionGateway;
import com.aierview.application.usecase.contract.AI.IGenerateQuestion;
import com.aierview.application.usecase.contract.question.ISaveQuestions;
import com.aierview.domain.entity.GenerateQuestionareParams;
import com.aierview.domain.entity.Question;
import com.aierview.domain.exceptions.UnexpectedException;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GenerateQuestion implements IGenerateQuestion {
    private final IGenerateQuestionGateway gateway;
    private final ISaveQuestions saveQuestions;

    @Override
    public List<Question> generate(GenerateQuestionareParams params) throws UnexpectedException {
        List<Question> questions = gateway.generate(params);
        if (List.of(questions).isEmpty())
            throw new UnexpectedException("An error occurred, please contact the administrator!");
        return saveQuestions.save(questions);
    }
}
