package com.aierview.application.usecase.contract;

import com.aierview.domain.entity.GenerateQuestionParams;
import com.aierview.domain.entity.Question;
import com.aierview.domain.exceptions.UnexpectedException;

import java.util.List;

public interface IGenerateQuestion {
    List<Question> generate(GenerateQuestionParams params) throws UnexpectedException;
}
