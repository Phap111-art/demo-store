package com.example.projectdemogit.exception;

public class CustomCloudinaryException extends Exception {
    public CustomCloudinaryException(String message) {
        super("Cloudinary upload error: " + message);
    }
}