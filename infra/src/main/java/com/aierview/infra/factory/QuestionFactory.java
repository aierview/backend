package com.aierview.infra.factory;

import com.aierview.application.usecase.impl.GenerateQuestion;
import com.aierview.application.usecase.impl.SaveQuestions;
import com.aierview.infra.service.impl.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class QuestionFactory {
    private final QuestionService service;

    @Bean
    public GenerateQuestion generateQuestion() {
        return new GenerateQuestion(service, saveQuestions());
    }

    @Bean
    public SaveQuestions saveQuestions() {
        return new SaveQuestions(service);
    }
}
