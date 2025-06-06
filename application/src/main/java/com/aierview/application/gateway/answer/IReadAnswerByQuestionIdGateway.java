package com.aierview.application.gateway.answer;

import com.aierview.domain.entity.Answer;

public interface IReadAnswerByQuestionIdGateway {
    Answer read(String id);
}
