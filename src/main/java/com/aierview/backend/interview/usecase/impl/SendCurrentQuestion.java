package com.aierview.backend.interview.usecase.impl;

import com.aierview.backend.interview.domain.contract.cache.IInterviewCacheRepository;
import com.aierview.backend.interview.domain.contract.publisher.IInterviewWebSocketPublisher;
import com.aierview.backend.interview.domain.contract.repository.IQuestionRepository;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.entity.InterviewState;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.domain.exceptions.UnavailableNextQuestionException;
import com.aierview.backend.interview.domain.model.CurrentQuestion;
import com.aierview.backend.interview.usecase.contract.ISendCurrentQuestion;

public class SendCurrentQuestion implements ISendCurrentQuestion {
    private final IInterviewWebSocketPublisher interviewWebSocketPublisher;
    private final IInterviewCacheRepository interviewCacheRepository;
    private final IQuestionRepository questionRepository;

    public SendCurrentQuestion(IInterviewWebSocketPublisher interviewWebSocketPublisher, IInterviewCacheRepository interviewCacheRepository, IQuestionRepository questionRepository) {
        this.interviewWebSocketPublisher = interviewWebSocketPublisher;
        this.interviewCacheRepository = interviewCacheRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public void execute(CurrentQuestion currentQuestion) {
        Question existingQuestion = this.questionRepository.findById(currentQuestion.questionId())
                .orElseThrow(UnavailableNextQuestionException::new);
        existingQuestion.setAudioUrl(currentQuestion.audioUrl());
        this.questionRepository.save(existingQuestion);
        Interview interview = existingQuestion.getInterview();
        InterviewState interviewState = this.interviewCacheRepository.get(interview.getId());
        if (interviewState.isFirst()) {
            this.interviewWebSocketPublisher.execute(interview.getId(), currentQuestion);
            interviewState.setStatus(existingQuestion.getId(), "WAITING_FOR_CLIENT_ACK");
        } else {
            interviewState.setStatus(existingQuestion.getId(), "READY_FOR_SEND");
        }
        this.interviewCacheRepository.revalidate(interviewState.getInterviewId(), interviewState);
    }
}
