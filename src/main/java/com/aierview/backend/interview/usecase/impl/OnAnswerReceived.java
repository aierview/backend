package com.aierview.backend.interview.usecase.impl;

import com.aierview.backend.interview.domain.contract.bucket.IUploadBase64File;
import com.aierview.backend.interview.domain.contract.cache.IInterviewCacheRepository;
import com.aierview.backend.interview.domain.contract.publisher.IAnswerEventPublisher;
import com.aierview.backend.interview.domain.contract.publisher.IInterviewWebSocketPublisher;
import com.aierview.backend.interview.domain.contract.repository.IQuestionRepository;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.entity.InterviewState;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.domain.exceptions.UnavailableNextQuestionException;
import com.aierview.backend.interview.domain.model.AnswerEventPublisherPayload;
import com.aierview.backend.interview.domain.model.CurrentQuestion;
import com.aierview.backend.interview.domain.model.OnAnswerReceivedRequest;
import com.aierview.backend.interview.usecase.contract.IOnAnswerReceived;

public class OnAnswerReceived implements IOnAnswerReceived {
    private final IQuestionRepository questionRepository;
    private final IInterviewCacheRepository interviewCacheRepository;
    private final IInterviewWebSocketPublisher interviewWebSocketPublisher;
    private final IAnswerEventPublisher answerEventPublisher;
    private final IUploadBase64File uploadBase64File;

    public OnAnswerReceived(IQuestionRepository questionRepository, IInterviewCacheRepository interviewCacheRepository, IInterviewWebSocketPublisher interviewWebSocketPublisher, IAnswerEventPublisher answerEventPublisher, IUploadBase64File uploadBase64File) {
        this.questionRepository = questionRepository;
        this.interviewCacheRepository = interviewCacheRepository;
        this.interviewWebSocketPublisher = interviewWebSocketPublisher;
        this.answerEventPublisher = answerEventPublisher;
        this.uploadBase64File = uploadBase64File;
    }

    @Override
    public void execute(OnAnswerReceivedRequest request) {
        Question existingQuestion = this.questionRepository.findById(request.questionId())
                .orElseThrow(UnavailableNextQuestionException::new);
        Interview interview = existingQuestion.getInterview();
        InterviewState interviewState = this.interviewCacheRepository.get(interview.getId());
        Question nextQuestion = interviewState.getNextQuestionReadyForSend();
        if (nextQuestion != null) {
            CurrentQuestion currentQuestion = new CurrentQuestion(nextQuestion.getId(), nextQuestion.getQuestion(), nextQuestion.getAudioUrl());
            this.interviewWebSocketPublisher.execute(interview.getId(), currentQuestion);
            interviewState.setStatus(currentQuestion.questionId(), "WAITING_FOR_CLIENT_ACK");
        }

        interviewState.setStatus(existingQuestion.getId(), "ANSWERED");
        this.interviewCacheRepository.revalidate(interview.getId(), interviewState);
        String filename = this.uploadBase64File.execute(request.base64Answer());
        AnswerEventPublisherPayload payload = new AnswerEventPublisherPayload(request.questionId(), filename);
        this.answerEventPublisher.publish(payload);
    }
}
