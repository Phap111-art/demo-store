package com.example.projectdemogit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.AuthenticationException;

@Getter
public class AuthException extends AuthenticationException {
    private HttpStatus httpStatus;

    public AuthException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}