package com.aierview.backend.auth.domain.exceptions;

public class InvalidCredentialException extends RuntimeException {
    public InvalidCredentialException() {
        super("Email or password is incorrect!");
    }
}
