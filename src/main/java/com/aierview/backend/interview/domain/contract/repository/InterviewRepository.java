package com.aierview.backend.interview.domain.contract.repository;

import com.aierview.backend.interview.domain.entity.Interview;

public interface InterviewRepository {
    Interview save(Interview interview);
}
