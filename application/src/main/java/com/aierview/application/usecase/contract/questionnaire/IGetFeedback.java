package com.aierview.application.usecase.contract.questionnaire;

import com.aierview.domain.entity.QuestionnaireResponse;
import com.aierview.domain.exceptions.BusinessException;

import java.util.List;

public interface IGetFeedback {
    List<QuestionnaireResponse> get(String questionareId) throws BusinessException;
}
