package com.aierview.application.usecase.impl.question;

import com.aierview.application.gateway.question.IAnswerQuestionGateway;
import com.aierview.application.usecase.contract.AI.IGenerateFeedback;
import com.aierview.application.usecase.contract.answer.IUpdateAnswer;
import com.aierview.application.usecase.contract.question.IAnswerQuestion;
import com.aierview.application.usecase.contract.question.IReadQuestionById;
import com.aierview.domain.entity.Answer;
import com.aierview.domain.exceptions.NotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AnswerQuestion implements IAnswerQuestion {
    private final IAnswerQuestionGateway gateway;
    private final IReadQuestionById readQuestionById;
    private final IGenerateFeedback generateFeedback;
    private final IUpdateAnswer update;

    @Override
    public void answer(Answer answer) throws NotFoundException {
        // throws not found if question does not exist
        readQuestionById.read(answer.getQuestionId());
        answer = gateway.answer(answer);
        String feedback = generateFeedback.generate(answer);
        answer.setFeedback(feedback);
        update.update(answer);
    }
}
