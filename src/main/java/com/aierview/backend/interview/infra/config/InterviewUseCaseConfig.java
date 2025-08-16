package com.aierview.backend.interview.infra.config;

import com.aierview.backend.interview.domain.contract.IA.IGenerateQuestions;
import com.aierview.backend.interview.domain.contract.cache.IInterviewCacheRepository;
import com.aierview.backend.interview.domain.contract.publisher.IInterviewEventPublisher;
import com.aierview.backend.interview.domain.contract.publisher.IInterviewWebSocketPublisher;
import com.aierview.backend.interview.domain.contract.repository.IInterviewRepository;
import com.aierview.backend.interview.domain.contract.repository.IQuestionRepository;
import com.aierview.backend.interview.domain.contract.user.IGetLoggedUser;
import com.aierview.backend.interview.usecase.contract.IBeginInterview;
import com.aierview.backend.interview.usecase.contract.IOnAnswerReceived;
import com.aierview.backend.interview.usecase.contract.IOnQuestionReceived;
import com.aierview.backend.interview.usecase.contract.ISendCurrentQuestion;
import com.aierview.backend.interview.usecase.impl.BeginInterview;
import com.aierview.backend.interview.usecase.impl.OnAnswerReceived;
import com.aierview.backend.interview.usecase.impl.OnQuestionReceived;
import com.aierview.backend.interview.usecase.impl.SendCurrentQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"!test-interview-db-error"})
@RequiredArgsConstructor
public class InterviewUseCaseConfig {
    private final IGetLoggedUser getLoggedUser;
    private final IInterviewRepository interviewRepository;
    private final IGenerateQuestions generateQuestions;
    private final IQuestionRepository questionRepository;
    private final IInterviewCacheRepository interviewCacheRepository;
    private final IInterviewEventPublisher interviewEventPublisher;
    private final IInterviewWebSocketPublisher interviewWebSocketPublisher;

    @Bean
    public IBeginInterview beginInterview() {
        return new BeginInterview(getLoggedUser, interviewRepository, generateQuestions,
                questionRepository, interviewCacheRepository, interviewEventPublisher);
    }

    @Bean
    public ISendCurrentQuestion sendCurrentQuestion() {
        return new SendCurrentQuestion(interviewWebSocketPublisher, interviewCacheRepository,
                questionRepository
        );
    }

    @Bean
    public IOnQuestionReceived onQuestionReceived() {
        return new OnQuestionReceived(questionRepository, interviewCacheRepository, interviewEventPublisher);
    }

    @Bean
    public IOnAnswerReceived onAnswerReceived() {
        return new OnAnswerReceived(questionRepository, interviewCacheRepository, interviewWebSocketPublisher);
    }
}
