package com.example.projectdemogit.service;

import com.example.projectdemogit.dtos.request.user.SendMailDto;
import com.example.projectdemogit.dtos.request.user.ResetPasswordUser;
import com.example.projectdemogit.dtos.response.CustomResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

public interface EmailService {
    CustomResponse sendMail(SendMailDto dto, BindingResult result, HttpServletRequest request);

    CustomResponse resetPassword(ResetPasswordUser dto, BindingResult result, String newPassword);
}
