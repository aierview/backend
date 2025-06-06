package com.aierview.application.usecase.contract.questionnaire;

import com.aierview.domain.entity.GenerateQuestionareParams;
import com.aierview.domain.entity.Questionnaire;
import com.aierview.domain.exceptions.UnexpectedException;

public interface IGenerateQuestionare {
    Questionnaire generate(GenerateQuestionareParams params) throws UnexpectedException;
}
