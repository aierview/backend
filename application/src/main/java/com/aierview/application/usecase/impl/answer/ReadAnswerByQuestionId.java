package com.aierview.application.usecase.impl.answer;

import com.aierview.application.gateway.answer.IReadAnswerByQuestionIdGateway;
import com.aierview.application.usecase.contract.answer.IReadAnswerByQuestionId;
import com.aierview.domain.entity.Answer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReadAnswerByQuestionId implements IReadAnswerByQuestionId {
    private final IReadAnswerByQuestionIdGateway gateway;

    @Override
    public Answer read(String id) {
        return gateway.read(id);
    }
}
