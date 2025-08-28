package com.aierview.backend.auth.domain.exceptions;

public class InvalidGoogleIdTokenException extends RuntimeException {
    public InvalidGoogleIdTokenException() {
        super("Invalid Google account, please provide a valid Google account.");
    }
}
