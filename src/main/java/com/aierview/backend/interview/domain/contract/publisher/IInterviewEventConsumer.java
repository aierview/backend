package com.aierview.backend.interview.domain.contract.publisher;

import com.aierview.backend.interview.domain.model.CurrentQuestion;

public interface IInterviewEventConsumer {
    public void consume(CurrentQuestion message);
}
