package com.aierview.application.usecase.contract.question;

import com.aierview.domain.entity.Answer;
import com.aierview.domain.exceptions.NotFoundException;

public interface IAnswerQuestion {
    void answer(Answer answer) throws NotFoundException;
}
