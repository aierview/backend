package com.aierview.application.gateway;

import com.aierview.domain.entity.Question;

import java.util.List;

public interface ISaveQuestionsGateway {
    List<Question> save(List<Question> questions);
}
