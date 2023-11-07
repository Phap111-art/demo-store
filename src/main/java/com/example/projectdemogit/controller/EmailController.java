package com.example.projectdemogit.controller;

import com.example.projectdemogit.dtos.request.user.ResetPasswordUser;

import com.example.projectdemogit.dtos.request.user.SendMailDto;
import com.example.projectdemogit.dtos.response.CustomResponse;
import com.example.projectdemogit.service.EmailService;
import com.example.projectdemogit.utils.URLUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forgot")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-mail")
    public ResponseEntity<CustomResponse> sendMail(@RequestBody @Valid SendMailDto dto, BindingResult result, HttpServletRequest request) {
        return ResponseEntity.ok(emailService.sendMail(dto, result, request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<CustomResponse> resetPassword(@RequestParam String token, @RequestBody @Valid ResetPasswordUser dto, BindingResult result) {

        return ResponseEntity.ok(emailService.resetPassword(dto,result,token));
    }
}
