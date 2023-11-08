package com.example.projectdemogit.exception;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;

public class SendMailException extends MessagingException {


    public SendMailException(String message) {
        super(message);
    }
}