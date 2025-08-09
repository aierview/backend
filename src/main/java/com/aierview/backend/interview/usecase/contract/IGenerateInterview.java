package com.aierview.backend.interview.usecase.contract;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;

public interface IGenerateInterview {
    Interview execute(BeginInterviewRequest request, UserRef user);
}
