package com.aierview.backend.auth.infra.middleware;

import com.aierview.backend.auth.domain.exceptions.EmailAlreadyInUseException;
import com.aierview.backend.auth.domain.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        Response response = new Response(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<Response> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex) {
        Response response = new Response(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleOtherExceptions(Exception ex) {
        log.error(ex.getMessage());
        Response response = new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please try again later.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

