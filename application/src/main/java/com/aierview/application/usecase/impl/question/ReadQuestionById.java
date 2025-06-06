package com.aierview.application.usecase.impl.question;

import com.aierview.application.gateway.question.IReadQuestionByIdGateway;
import com.aierview.application.usecase.contract.question.IReadQuestionById;
import com.aierview.domain.entity.Question;
import com.aierview.domain.exceptions.NotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReadQuestionById implements IReadQuestionById {
    private final IReadQuestionByIdGateway gateway;

    @Override
    public Question read(String id) throws NotFoundException {
        Question question = gateway.read(id);
        if (question == null) throw new NotFoundException("Could not found question with id " + id);
        return question;
    }
}
