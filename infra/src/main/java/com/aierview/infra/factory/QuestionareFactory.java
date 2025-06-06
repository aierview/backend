package com.aierview.infra.factory;

import com.aierview.application.usecase.contract.answer.IReadAnswerByQuestionId;
import com.aierview.application.usecase.contract.question.ISaveQuestions;
import com.aierview.application.usecase.contract.questionnaire.IGenerateQuestionnaire;
import com.aierview.application.usecase.contract.questionnaire.IGetFeedback;
import com.aierview.application.usecase.impl.AI.GenerateQuestion;
import com.aierview.application.usecase.impl.questionnaire.GenerateQuestionnaire;
import com.aierview.application.usecase.impl.questionnaire.GetFeedback;
import com.aierview.infra.service.AnswerService;
import com.aierview.infra.service.GeminiService;
import com.aierview.infra.service.QuestionService;
import com.aierview.infra.service.QuestionareService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class QuestionareFactory {
    //services
    private final GeminiService geminiService;
    private final QuestionareService questionareService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    //use cases
    private final ISaveQuestions saveQuestions;
    private final IReadAnswerByQuestionId readAnswerByQuestionId;

    @Bean
    public IGenerateQuestionnaire generateQuestionare() {
        return new GenerateQuestionnaire(questionareService, new GenerateQuestion(geminiService, saveQuestions));
    }

    @Bean
    public IGetFeedback getFeedback() {
        return new GetFeedback(questionareService, readAnswerByQuestionId);
    }
}
