package com.aierview.backend.interview.usecase.impl;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.interview.domain.contract.repository.InterviewRepository;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.enums.InterviewStatus;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;
import com.aierview.backend.interview.usecase.contract.IBeginInterview;
import com.aierview.backend.interview.usecase.contract.IGenerateQuestions;
import com.aierview.backend.interview.usecase.contract.IGetLoggedUser;

import java.time.LocalDateTime;

public class BeginInterview implements IBeginInterview {
    private final IGetLoggedUser getLoggedUser;
    private final InterviewRepository interviewRepository;
    private final IGenerateQuestions generateQuestions;

    public BeginInterview(IGetLoggedUser getLoggedUser, InterviewRepository interviewRepository, IGenerateQuestions generateQuestions) {
        this.getLoggedUser = getLoggedUser;
        this.interviewRepository = interviewRepository;
        this.generateQuestions = generateQuestions;
    }

    @Override
    public Interview execute(BeginInterviewRequest request) {
        UserRef user = this.getLoggedUser.execute();
        Interview interview = this.buildInterview(request,user);
        interview = this.interviewRepository.save(interview);
        this.generateQuestions.execute(request,interview.getId());
        return null;
    }

    private Interview buildInterview(BeginInterviewRequest request,UserRef user) {
        return Interview
                .builder()
                .stack(request.getStack())
                .user(user)
                .role(request.getRole())
                .level(request.getInterviewLevel())
                .status(InterviewStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
