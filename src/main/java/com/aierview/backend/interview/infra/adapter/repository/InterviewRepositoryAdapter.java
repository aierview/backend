package com.aierview.backend.interview.infra.adapter.repository;

import com.aierview.backend.interview.domain.contract.repository.IInterviewRepository;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.infra.mapper.InterviewMapper;
import com.aierview.backend.interview.infra.persistence.entity.InterviewJpaEntity;
import com.aierview.backend.interview.infra.persistence.repository.InterviewJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewRepositoryAdapter implements IInterviewRepository {
    private final InterviewMapper interviewMapper;
    private final InterviewJpaRepository interviewJpaRepository;

    @Override
    public Interview save(Interview interview) {
        InterviewJpaEntity entity = this.interviewMapper.interviewToInterviewJpaEntity(interview);
        entity = this.interviewJpaRepository.save(entity);
        return this.interviewMapper.interviewJpaEntityToInterview(entity);
    }

    @Override
    public Interview update(Interview interview) {
        return this.save(interview);
    }
}
