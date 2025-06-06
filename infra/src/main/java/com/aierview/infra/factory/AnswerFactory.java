package com.aierview.infra.factory;

import com.aierview.application.usecase.contract.answer.IReadAnswerByQuestionId;
import com.aierview.application.usecase.impl.answer.ReadAnswerByQuestionId;
import com.aierview.infra.service.AnswerService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class AnswerFactory {
    private final AnswerService answerService;

    @Bean
    public IReadAnswerByQuestionId readAnswerByQuestionId() {
        return new ReadAnswerByQuestionId(answerService);
    }
}
