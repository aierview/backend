package com.aierview.backend.interview.infra.mapper;


import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.infra.persistence.entity.InterviewJpaEntity;
import org.mapstruct.Mapper;

@Mapper()
public interface InterviewMapper {
    InterviewJpaEntity interviewToInterviewJpaEntity(Interview interview);

    Interview interviewJpaEntityToInterview(InterviewJpaEntity interviewJpaEntity);
}