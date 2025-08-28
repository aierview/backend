package com.aierview.backend.interview.usecase.contract;

import com.aierview.backend.interview.domain.model.OnQuestionReceivedRequest;

public interface IOnQuestionReceived {
    void execute(OnQuestionReceivedRequest request);
}
