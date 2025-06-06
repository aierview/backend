package com.aierview.application.usecase.contract.AI;

import com.aierview.domain.entity.GenerateQuestionnaireParams;
import com.aierview.domain.entity.Question;
import com.aierview.domain.exceptions.UnexpectedException;

import java.util.List;

public interface IGenerateQuestion {
    List<Question> generate(GenerateQuestionnaireParams params) throws UnexpectedException;
}
