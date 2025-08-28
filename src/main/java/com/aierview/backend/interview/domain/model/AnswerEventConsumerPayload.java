package com.aierview.backend.interview.domain.model;

public record AnswerEventConsumerPayload(Long questionId, String answerText) {
}