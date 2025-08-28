package com.aierview.backend.interview.domain.model;

public record OnAnswerReceivedRequest(Long questionId, String base64Answer) {
}
