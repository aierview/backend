package com.aierview.backend.interview.infra.config;

import com.aierview.backend.interview.domain.contract.IA.IGenerateQuestions;
import com.aierview.backend.interview.domain.contract.IA.IIAGenerateFeedback;
import com.aierview.backend.interview.domain.contract.bucket.IUploadBase64File;
import com.aierview.backend.interview.domain.contract.cache.IInterviewCacheRepository;
import com.aierview.backend.interview.domain.contract.publisher.IAnswerEventPublisher;
import com.aierview.backend.interview.domain.contract.publisher.IInterviewEventPublisher;
import com.aierview.backend.interview.domain.contract.publisher.IInterviewWebSocketPublisher;
import com.aierview.backend.interview.domain.contract.repository.IInterviewRepository;
import com.aierview.backend.interview.domain.contract.repository.IQuestionRepository;
import com.aierview.backend.interview.domain.contract.user.IGetLoggedUser;
import com.aierview.backend.interview.usecase.contract.*;
import com.aierview.backend.interview.usecase.impl.*;
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
    private final IAnswerEventPublisher answerEventPublisher;
    private final IUploadBase64File uploadBase64File;
    private final IIAGenerateFeedback iiaGenerateFeedback;

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
        return new OnAnswerReceived(questionRepository,
                interviewCacheRepository, interviewWebSocketPublisher, answerEventPublisher, uploadBase64File);
    }

    @Bean
    public IGenerateFeedback generateFeedback (){
        return new GenerateFeedback(questionRepository, iiaGenerateFeedback);
    }
}
