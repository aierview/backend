package com.aierview.application.usecase.contract.AI;

import com.aierview.domain.entity.Answer;
import com.aierview.domain.exceptions.NotFoundException;

public interface IGenerateFeedback {
    void sendToAsyncGeneration(Answer answer) throws NotFoundException;
}
