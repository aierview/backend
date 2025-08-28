package com.aierview.backend.interview.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterviewState {
    private Long interviewId;
    private List<Question> questions;
    private int currentQuestionIndex;
    private Map<Long, String> questionStatus = new HashMap<>();

    public InterviewState(Long interviewId, List<Question> questions) {
        this.interviewId = interviewId;
        this.questions = questions;
        for (Question q : questions) {
            questionStatus.put(q.getId(), "PENDING");
        }
    }

    public boolean isFirst() {
        return currentQuestionIndex == 0;
    }

    public boolean hasNextQuestion() {
        return currentQuestionIndex + 1 < questions.size();
    }

    public Question peekNextQuestion() {
        if (!hasNextQuestion()) return null;
        return questions.get(currentQuestionIndex + 1);
    }

    public void setStatus(Long questionId, String status) {
        questionStatus.put(questionId, status);
    }

    public String getStatus(Long questionId) {
        return questionStatus.getOrDefault(questionId, "PENDING");
    }

    public Question advanceToNextQuestion() {
        if (!hasNextQuestion()) return null;
        currentQuestionIndex++;
        return questions.get(currentQuestionIndex);
    }

    public Question getNextQuestionReadyForSend() {
        for (int i = currentQuestionIndex + 1; i < questions.size(); i++) {
            Question q = questions.get(i);
            String status = questionStatus.get(q.getId());
            if ("READY_FOR_SEND".equals(status)) {
                currentQuestionIndex = i;
                return q;
            }
        }
        return null;
    }
}

