package com.aierview.backend.interview.usecase.impl;

import com.aierview.backend.interview.domain.contract.cache.IInterviewCacheRepository;
import com.aierview.backend.interview.domain.contract.publisher.IInterviewEventPublisher;
import com.aierview.backend.interview.domain.contract.repository.IQuestionRepository;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.entity.InterviewState;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.domain.exceptions.UnavailableNextQuestionException;
import com.aierview.backend.interview.domain.model.OnQuestionReceivedRequest;
import com.aierview.backend.interview.usecase.contract.IOnQuestionReceived;

public class OnQuestionReceived implements IOnQuestionReceived {
    private final IQuestionRepository questionRepository;
    private final IInterviewCacheRepository interviewCacheRepository;
    private final IInterviewEventPublisher interviewEventPublisher;

    public OnQuestionReceived(IQuestionRepository questionRepository, IInterviewCacheRepository interviewCacheRepository, IInterviewEventPublisher interviewEventPublisher) {
        this.questionRepository = questionRepository;
        this.interviewCacheRepository = interviewCacheRepository;
        this.interviewEventPublisher = interviewEventPublisher;
    }

    @Override
    public void execute(OnQuestionReceivedRequest request) {
        Question existingQuestion = this.questionRepository.findById(request.questionId())
                .orElseThrow(UnavailableNextQuestionException::new);

        Interview interview = existingQuestion.getInterview();
        InterviewState interviewState = this.interviewCacheRepository.get(interview.getId());
        interviewState.setStatus(request.questionId(), "READY_FOR_NEXT_PROCESS");
        this.interviewCacheRepository.revalidate(interview.getId(), interviewState);
        if (!interviewState.hasNextQuestion()) return;
        Question nextQuestion = interviewState.peekNextQuestion();
        this.interviewEventPublisher.publish(nextQuestion);
        interviewState.setStatus(nextQuestion.getId(), "WAITING_FOR_PREP");
        interviewState.advanceToNextQuestion();
        this.interviewCacheRepository.revalidate(interview.getId(), interviewState);
    }
}
