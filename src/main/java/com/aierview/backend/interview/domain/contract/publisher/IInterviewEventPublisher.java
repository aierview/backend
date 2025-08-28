package com.aierview.backend.interview.domain.contract.publisher;

import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.domain.model.InterviewEventPublisherPayload;

public interface IInterviewEventPublisher {
    void publish(InterviewEventPublisherPayload payload);
}
