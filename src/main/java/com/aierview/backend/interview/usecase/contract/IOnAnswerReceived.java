package com.aierview.backend.interview.usecase.contract;

import com.aierview.backend.interview.domain.model.OnAnswerReceivedRequest;

public interface IOnAnswerReceived {
    void execute(OnAnswerReceivedRequest request);
}
