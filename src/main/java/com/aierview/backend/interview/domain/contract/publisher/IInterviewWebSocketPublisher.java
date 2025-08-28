package com.aierview.backend.interview.domain.contract.publisher;

import com.aierview.backend.interview.domain.model.CurrentQuestion;

public interface IInterviewWebSocketPublisher {
    void execute(Long interviewId, CurrentQuestion currentQuestion);
}
