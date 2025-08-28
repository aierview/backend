package com.aierview.backend.interview.domain.contract.publisher;

import com.aierview.backend.interview.domain.model.CurrentQuestion;
import com.aierview.backend.interview.domain.model.InterviewEventConsumerPayload;

public interface IInterviewEventConsumer {
    void consume(InterviewEventConsumerPayload payload);
}
