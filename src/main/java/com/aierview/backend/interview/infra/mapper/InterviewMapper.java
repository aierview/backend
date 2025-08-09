package com.aierview.backend.interview.infra.mapper;


import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.infra.persistence.entity.InterviewJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper()
public interface InterviewMapper {
    InterviewJpaEntity interviewToInterviewJpaEntity(Interview interview);
    Interview interviewJpaEntityToInterview(InterviewJpaEntity interviewJpaEntity);
}