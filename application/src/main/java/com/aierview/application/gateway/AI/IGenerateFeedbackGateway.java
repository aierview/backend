package com.aierview.application.gateway.AI;

import com.aierview.domain.entity.Answer;

public interface IGenerateFeedbackGateway {
    void sendToAsyncGeneration(Answer answer);
}
