package com.aierview.application.gateway.question;

import com.aierview.domain.entity.Question;

public interface IReadQuestionByIdGateway {
    Question read(String id);
}
