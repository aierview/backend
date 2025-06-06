package com.aierview.application.usecase.contract.question;

import com.aierview.domain.entity.Question;
import com.aierview.domain.exceptions.NotFoundException;

public interface IReadQuestionById {
    Question read(String id) throws NotFoundException;
}
