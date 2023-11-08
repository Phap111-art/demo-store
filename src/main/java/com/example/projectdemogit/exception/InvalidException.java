package com.example.projectdemogit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

public class InvalidException extends BadCredentialsException {
    private HttpStatus httpStatus;

    public InvalidException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }
}
