package com.aierview.backend.interview.usecase.contract;

import com.aierview.backend.interview.domain.model.BeginInterviewRequest;

public interface IBeginInterview {
    void execute(BeginInterviewRequest request);
}
