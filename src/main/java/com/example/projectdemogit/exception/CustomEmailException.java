package com.example.projectdemogit.exception;

import jakarta.mail.MessagingException;
public class CustomEmailException extends MessagingException {

    public CustomEmailException(String message) {
        super(message);
    }
}