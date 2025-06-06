package com.aierview.application.gateway.AI;

import com.aierview.domain.entity.GenerateQuestionnaireParams;
import com.aierview.domain.entity.Question;

import java.util.List;

public interface IGenerateQuestionGateway {
    List<Question> generate(GenerateQuestionnaireParams params);
}
