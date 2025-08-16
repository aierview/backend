package com.aierview.backend.interview.usecase.contract;

import com.aierview.backend.interview.domain.model.BeginInterviewRequest;

public interface IBeginInterview {
    Long execute(BeginInterviewRequest request);
}
