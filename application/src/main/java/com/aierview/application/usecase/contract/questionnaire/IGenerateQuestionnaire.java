package com.aierview.application.usecase.contract.questionnaire;

import com.aierview.domain.entity.GenerateQuestionnaireParams;
import com.aierview.domain.entity.Questionnaire;
import com.aierview.domain.exceptions.UnexpectedException;

public interface IGenerateQuestionnaire {
    Questionnaire generate(GenerateQuestionnaireParams params) throws UnexpectedException;
}
