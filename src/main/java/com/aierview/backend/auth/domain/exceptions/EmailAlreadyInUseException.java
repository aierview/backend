package com.aierview.backend.auth.domain.exceptions;

public class EmailAlreadyInUseException extends  RuntimeException{
    public EmailAlreadyInUseException(String email) {
        super("The email " + email + " is already in use.");
    }
}
