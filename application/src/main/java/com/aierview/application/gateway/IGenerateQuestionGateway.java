package com.aierview.application.gateway;

import com.aierview.domain.entity.GenerateQuestionParams;
import com.aierview.domain.entity.Question;

import java.util.List;

public interface IGenerateQuestionGateway {
    List<Question> generate(GenerateQuestionParams params);
}
