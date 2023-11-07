package com.example.projectdemogit.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class ValidationUtils {

    public static String getValidationErrorString(BindingResult bindingResult) {
        List<String> errorMessages = new ArrayList<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            errorMessages.add(fieldError.getDefaultMessage());
        }

        return String.join(", ", errorMessages);
    }
}