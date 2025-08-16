package com.aierview.backend.interview.domain.contract.publisher;

import com.aierview.backend.interview.domain.entity.Question;

public interface IInterviewEventPublisher {
    void publish(Question question);
}
