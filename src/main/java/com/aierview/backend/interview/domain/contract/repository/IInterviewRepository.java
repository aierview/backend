package com.aierview.backend.interview.domain.contract.repository;

import com.aierview.backend.interview.domain.entity.Interview;

public interface IInterviewRepository {
    Interview save(Interview interview);

    Interview update(Interview interview);
}
