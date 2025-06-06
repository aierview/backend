package com.aierview.application.gateway.AI;

import com.aierview.domain.entity.Answer;

public interface IGenerateFeedbackGateway {
    String generate(Answer answer);
}
