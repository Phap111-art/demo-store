package com.example.projectdemogit.exception;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonLocation;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class JsonException extends RuntimeException {
    private HttpStatus httpStatus;

    public JsonException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }



}
