package com.aierview.backend.interview.domain.contract.cache;

import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.entity.InterviewState;

public interface IInterviewCacheRepository {
    void put(Interview interview);

    InterviewState get(Long interviewId);

    void remove(Long interviewId);
}
