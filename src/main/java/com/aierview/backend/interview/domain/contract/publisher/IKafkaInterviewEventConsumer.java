package com.aierview.backend.interview.domain.contract.publisher;

import com.aierview.backend.interview.domain.model.CurrentQuestion;

public interface IKafkaInterviewEventConsumer {
    public void consume(CurrentQuestion message);
}
