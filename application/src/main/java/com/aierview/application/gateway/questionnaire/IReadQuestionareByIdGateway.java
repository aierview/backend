package com.aierview.application.gateway.questionnaire;

import com.aierview.domain.entity.Questionnaire;

public interface IReadQuestionareByIdGateway {
    Questionnaire get(String questionareId);
}
