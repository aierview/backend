package com.aierview.backend.interview.usecase.contract;

import com.aierview.backend.interview.domain.model.CurrentQuestion;

public interface ISendCurrentQuestion {
    void execute(CurrentQuestion currentQuestion);
}
