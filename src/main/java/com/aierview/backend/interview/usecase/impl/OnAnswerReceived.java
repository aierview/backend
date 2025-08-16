package com.aierview.backend.interview.usecase.impl;

import com.aierview.backend.interview.domain.contract.cache.IInterviewCacheRepository;
import com.aierview.backend.interview.domain.contract.publisher.IInterviewWebSocketPublisher;
import com.aierview.backend.interview.domain.contract.repository.IQuestionRepository;
import com.aierview.backend.interview.domain.entity.Interview;
import com.aierview.backend.interview.domain.entity.InterviewState;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.domain.exceptions.UnavailableNextQuestionException;
import com.aierview.backend.interview.domain.model.CurrentQuestion;
import com.aierview.backend.interview.usecase.contract.IOnAnswerReceived;

public class OnAnswerReceived implements IOnAnswerReceived {

    private final IQuestionRepository questionRepository;
    private final IInterviewCacheRepository interviewCacheRepository;
    private final IInterviewWebSocketPublisher interviewWebSocketPublisher;

    public OnAnswerReceived(IQuestionRepository questionRepository, IInterviewCacheRepository interviewCacheRepository, IInterviewWebSocketPublisher interviewWebSocketPublisher) {
        this.questionRepository = questionRepository;
        this.interviewCacheRepository = interviewCacheRepository;
        this.interviewWebSocketPublisher = interviewWebSocketPublisher;
    }

    @Override
    public void execute(String answer, Long questionId) {
        Question existingQuestion = this.questionRepository.findById(questionId)
                .orElseThrow(UnavailableNextQuestionException::new);
        //implement processing question with stt
        existingQuestion.setAnswer(answer);
        this.questionRepository.save(existingQuestion);

        Interview interview = existingQuestion.getInterview();
        InterviewState interviewState = this.interviewCacheRepository.get(interview.getId());
        interviewState.setStatus(existingQuestion.getId(), "ANSWERED");
        this.interviewCacheRepository.revalidate(interview.getId(), interviewState);
        Question nextQuestion = interviewState.getNextQuestionReadyForSend();
        CurrentQuestion currentQuestion = new CurrentQuestion(nextQuestion.getId(), nextQuestion.getQuestion(), nextQuestion.getAudioUrl());
        this.interviewWebSocketPublisher.execute(interview.getId(), currentQuestion);
        interviewState.setStatus(nextQuestion.getId(), "WAITING_FOR_CLIENT_ACK");
        this.interviewCacheRepository.revalidate(interview.getId(), interviewState);
    }
}
