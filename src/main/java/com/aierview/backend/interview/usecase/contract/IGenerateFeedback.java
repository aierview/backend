package com.aierview.backend.interview.usecase.contract;

import com.aierview.backend.interview.domain.model.AnswerEventConsumerPayload;

public interface IGenerateFeedback {
    void execute(AnswerEventConsumerPayload payload);
}
