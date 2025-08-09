package com.aierview.backend.interview.infra.dto;

public record FirstQuestionEvent(Long interviewId, Long questionId, String text) {
}