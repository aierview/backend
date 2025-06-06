package com.aierview.application.gateway.AI;

import com.aierview.domain.entity.GenerateQuestionareParams;
import com.aierview.domain.entity.Question;

import java.util.List;

public interface IGenerateQuestionGateway {
    List<Question> generate(GenerateQuestionareParams params);
}
