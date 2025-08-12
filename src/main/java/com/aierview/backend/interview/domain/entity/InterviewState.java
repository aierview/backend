package com.aierview.backend.interview.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class InterviewState {
    private Long interviewId;
    private List<Question> questions;
    private int currentQuestionIndex = -1;

    public InterviewState(Long interviewId, List<Question> questions) {
        this.interviewId = interviewId;
        this.questions = questions;
    }

    public boolean hasNextQuestion() {
        return currentQuestionIndex + 1 < questions.size();
    }

    public Question getNextQuestion() {
        if (!hasNextQuestion()) return null;
        return questions.get(currentQuestionIndex + 1);
    }
}
