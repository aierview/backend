package com.aierview.application.usecase.impl.questionnaire;

import com.aierview.application.usecase.contract.answer.IReadAnswerByQuestionId;
import com.aierview.application.usecase.contract.questionnaire.IGetFeedback;
import com.aierview.application.usecase.contract.questionnaire.IReadQuestionareById;
import com.aierview.domain.entity.Answer;
import com.aierview.domain.entity.Question;
import com.aierview.domain.entity.QuestionnaireResponse;
import com.aierview.domain.entity.Questionnaire;
import com.aierview.domain.exceptions.BusinessException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GetFeedback implements IGetFeedback {
    private final IReadQuestionareById readQuestionareById;
    private final IReadAnswerByQuestionId readAnswerByQuestionId;

    @Override
    public List<QuestionnaireResponse> get(String questionareId) throws BusinessException {
        Questionnaire questionnaire = readQuestionareById.read(questionareId);
        return buildQuestionareResponses(questionnaire.getQuestions());
    }

    private QuestionnaireResponse buildQuestionareResponse(String question, String answer, String feedback) {
        return QuestionnaireResponse.builder().question(question).answer(answer).feedback(feedback).build();
    }

    private List<QuestionnaireResponse> buildQuestionareResponses(List<Question> questions) throws BusinessException {
        boolean hasUnanswered = questions.stream()
                .anyMatch(question -> readAnswerByQuestionId.read(question.getId()) == null);

        if (hasUnanswered) {
            throw new BusinessException("You have to answer all questions first!");
        }

        return questions.stream()
                .map(question -> {
                    Answer answer = readAnswerByQuestionId.read(question.getId());
                    return buildQuestionareResponse(
                            question.getStatement(),
                            answer.getAnswerText(),
                            answer.getFeedback()
                    );
                })
                .collect(Collectors.toList());
    }

}
