package com.aierview.infra.factory;

import com.aierview.application.usecase.impl.AI.GenerateFeedback;
import com.aierview.application.usecase.impl.AI.GenerateQuestion;
import com.aierview.application.usecase.impl.answer.UpdateAnswer;
import com.aierview.application.usecase.impl.question.AnswerQuestion;
import com.aierview.application.usecase.impl.question.ReadQuestionById;
import com.aierview.application.usecase.impl.question.SaveQuestions;
import com.aierview.infra.service.GeminiService;
import com.aierview.infra.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class QuestionFactory {
    private final GeminiService geminiService;
    private final QuestionService questionService;

    @Bean
    public AnswerQuestion answerQuestion() {
        return new AnswerQuestion(questionService, readQuestionById(), generateFeedback(), updateAnswer());
    }

    @Bean
    public ReadQuestionById readQuestionById() {
        return new ReadQuestionById(questionService);
    }

    @Bean
    public GenerateQuestion generateQuestion() {
        return new GenerateQuestion(geminiService, saveQuestions());
    }

    @Bean
    public GenerateFeedback generateFeedback() {
        return new GenerateFeedback(geminiService, readQuestionById());
    }


    @Bean
    public SaveQuestions saveQuestions() {
        return new SaveQuestions(questionService);
    }

    @Bean
    public UpdateAnswer updateAnswer() {
        return new UpdateAnswer(questionService);
    }
}
