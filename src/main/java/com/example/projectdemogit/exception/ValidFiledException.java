package com.example.projectdemogit.exception;

import javax.validation.ValidationException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class ValidFiledException extends ValidationException {
    private HttpStatus httpStatus;

    public ValidFiledException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}