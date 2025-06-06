package com.aierview.application.usecase.contract.questionnaire;

import com.aierview.domain.entity.QuestionareResponse;
import com.aierview.domain.exceptions.BusinessException;

import java.util.List;

public interface IGetFeedback {
    List<QuestionareResponse> get(String questionareId) throws BusinessException;
}
