package com.aierview.backend.interview.infra.dto;

public record NextQuestionEvent(Long interviewId, Long questionId, String text) {
}