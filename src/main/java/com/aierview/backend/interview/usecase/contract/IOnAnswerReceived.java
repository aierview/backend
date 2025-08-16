package com.aierview.backend.interview.usecase.contract;

public interface IOnAnswerReceived {
    void execute(String answer, Long questionId);
}
