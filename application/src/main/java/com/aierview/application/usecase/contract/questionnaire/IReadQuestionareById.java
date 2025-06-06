package com.aierview.application.usecase.contract.questionnaire;

import com.aierview.domain.entity.Questionnaire;

public interface IReadQuestionareById {
    Questionnaire read(String questionareId);
}
