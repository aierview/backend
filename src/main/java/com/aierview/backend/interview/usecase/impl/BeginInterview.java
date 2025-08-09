package com.aierview.backend.interview.usecase.impl;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.interview.domain.contract.publisher.IInterviewEventPublisher;
import com.aierview.backend.interview.domain.contract.repository.IInterviewRepository;
import com.aierview.backend.interview.domain.contract.repository.IQuestionRepository;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.domain.enums.InterviewStatus;
import com.aierview.backend.interview.domain.model.BeginInterviewRequest;
import com.aierview.backend.interview.usecase.contract.IBeginInterview;
import com.aierview.backend.interview.usecase.contract.IGenerateQuestions;
import com.aierview.backend.interview.usecase.contract.IGetLoggedUser;

import java.time.LocalDateTime;
import java.util.List;

public class BeginInterview implements IBeginInterview {
    private final IGetLoggedUser getLoggedUser;
    private final IInterviewRepository interviewRepository;
    private final IGenerateQuestions generateQuestions;
    private final IQuestionRepository questionRepository;
    private final IInterviewEventPublisher interviewEventPublisher;

    public BeginInterview(IGetLoggedUser getLoggedUser, IInterviewRepository interviewRepository, IGenerateQuestions generateQuestions,
                          IQuestionRepository questionRepository, IInterviewEventPublisher interviewEventPublisher) {
        this.getLoggedUser = getLoggedUser;
        this.interviewRepository = interviewRepository;
        this.generateQuestions = generateQuestions;
        this.questionRepository = questionRepository;
        this.interviewEventPublisher = interviewEventPublisher;
    }

    @Override
    public void execute(BeginInterviewRequest request) {
        UserRef user = this.getLoggedUser.execute();
        Interview interview = this.buildInterview(request, user);
        interview = this.interviewRepository.save(interview);
        List<Question> questions = this.generateQuestions.execute(request, interview.getId());
        questions = this.questionRepository.saveAll(questions);
        interview.setQuestions(questions);
        interview.setStatus(InterviewStatus.STARTED);
        this.interviewRepository.update(interview);
        this.interviewEventPublisher.publishFirstQuestion(questions.getFirst());
    }

    private Interview buildInterview(BeginInterviewRequest request, UserRef user) {
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
