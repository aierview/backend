package com.aierview.backend.interview.domain.contract.publisher;

import com.aierview.backend.interview.domain.model.AnswerEventPublisherPayload;

public interface IAnswerEventPublisher {
    void publish(AnswerEventPublisherPayload payload);
}
