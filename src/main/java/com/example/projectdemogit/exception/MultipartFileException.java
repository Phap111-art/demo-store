package com.example.projectdemogit.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Data
public class MultipartFileException extends IOException {
    private HttpStatus httpStatus;

    public MultipartFileException(String message, HttpStatus  httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
