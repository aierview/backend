package com.aierview.application.usecase.impl.AI;

import com.aierview.application.gateway.AI.IGenerateFeedbackGateway;
import com.aierview.application.usecase.contract.AI.IGenerateFeedback;
import com.aierview.application.usecase.contract.question.IReadQuestionById;
import com.aierview.domain.entity.Answer;
import com.aierview.domain.exceptions.NotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GenerateFeedback implements IGenerateFeedback {
    private final IGenerateFeedbackGateway gateway;
    private final IReadQuestionById readQuestionById;

    @Override
    public String generate(Answer answer) throws NotFoundException {
        // throws not found if question does not exist
        readQuestionById.read(answer.getQuestionId());
        return gateway.generate(answer);
    }
}
