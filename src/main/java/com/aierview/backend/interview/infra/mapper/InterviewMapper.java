package com.aierview.backend.interview.infra.mapper;


import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.infra.persistence.entity.InterviewJpaEntity;
import org.mapstruct.Mapper;

@Mapper()
public interface InterviewMapper {
    InterviewJpaEntity mapToJpa(Interview interview);

    Interview mapToEntity(InterviewJpaEntity interviewJpaEntity);
}