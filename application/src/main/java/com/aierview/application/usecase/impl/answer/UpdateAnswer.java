package com.aierview.application.usecase.impl.answer;

import com.aierview.application.gateway.answer.IUpdateAnswerGateway;
import com.aierview.application.usecase.contract.answer.IUpdateAnswer;
import com.aierview.domain.entity.Answer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateAnswer implements IUpdateAnswer {
    private final IUpdateAnswerGateway gateway;

    @Override
    public void update(Answer answer) {
        gateway.update(answer);
    }
}
