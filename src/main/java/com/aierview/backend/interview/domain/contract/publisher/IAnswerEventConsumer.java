package com.aierview.backend.interview.domain.contract.publisher;

import com.aierview.backend.interview.domain.model.AnswerEventConsumerPayload;

public interface IAnswerEventConsumer {
    void consume(AnswerEventConsumerPayload payload);
}
