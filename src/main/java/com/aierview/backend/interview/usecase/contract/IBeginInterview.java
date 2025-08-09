package com.aierview.backend.interview.usecase.contract;

import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;

public interface IBeginInterview {
    Interview execute(BeginInterviewRequest request);
}
