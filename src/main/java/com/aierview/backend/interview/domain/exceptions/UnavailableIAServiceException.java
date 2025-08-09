package com.aierview.backend.interview.domain.exceptions;

public class UnavailableIAServiceException extends RuntimeException{
    public UnavailableIAServiceException () {
        super("We sorry! IA Service not available at this time, please try again later.");
    }
}
