package com.aierview.application.usecase.impl.questionnaire;

import com.aierview.application.gateway.questionnaire.IGenerateQuestionnaireGateway;
import com.aierview.application.usecase.contract.AI.IGenerateQuestion;
import com.aierview.application.usecase.contract.questionnaire.IGenerateQuestionare;
import com.aierview.domain.entity.GenerateQuestionareParams;
import com.aierview.domain.entity.Question;
import com.aierview.domain.entity.Questionnaire;
import com.aierview.domain.exceptions.UnexpectedException;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GenerateQuestionare implements IGenerateQuestionare {
    private final IGenerateQuestionnaireGateway gateway;
    private final IGenerateQuestion generateQuestion;

    @Override
    public Questionnaire generate(GenerateQuestionareParams params) throws UnexpectedException {
        String title = buildTitle(params);
        List<Question> questions = generateQuestion.generate(params);
        Questionnaire questionnaire = buildQuestionare(title, questions);
        return gateway.generate(questionnaire);
    }

    private String buildTitle(GenerateQuestionareParams params) {
        StringBuilder title = new StringBuilder();
        title
                .append("Entrivsta técnica para o cargo de Desenvolvedor ")
                .append(params.getLanguage()).append(" ")
                .append(params.getLevel()).append(" ")
                .append("com ").append(params.getFramework());
        return title.toString();
    }

    private Questionnaire buildQuestionare(String title, List<Question> questions) {
        return Questionnaire.builder().title(title).questions(questions).build();
    }
}
