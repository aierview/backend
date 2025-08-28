package com.aierview.backend.interview.domain.exceptions;

public class UnavailableNextQuestionException extends RuntimeException {
    public UnavailableNextQuestionException() {
        super("We sorry! We couldn't provide next question. Please try again.");
    }
}
