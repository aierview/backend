package com.aierview.application.gateway.questionnaire;

import com.aierview.domain.entity.Questionnaire;

public interface IGenerateQuestionnaireGateway {
    Questionnaire generate(Questionnaire questionnaire);
}
