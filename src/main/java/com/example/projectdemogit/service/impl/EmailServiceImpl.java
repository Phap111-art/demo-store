package com.example.projectdemogit.service.impl;

import com.example.projectdemogit.dtos.request.user.ResetPasswordUser;
import com.example.projectdemogit.dtos.request.user.SendMailDto;
import com.example.projectdemogit.dtos.response.CustomResponse;
import com.example.projectdemogit.entity.User;
import com.example.projectdemogit.exception.CustomException;
import com.example.projectdemogit.exception.SendMailException;
import com.example.projectdemogit.repository.UserRepository;
import com.example.projectdemogit.service.EmailService;
import com.example.projectdemogit.utils.URLUtil;
import com.example.projectdemogit.utils.ValidationUtils;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomResponse sendMail(SendMailDto dto, BindingResult result, HttpServletRequest request) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            Optional<User> existingEmail = userRepository.findByEmail(dto.getEmail());
            if (result.hasErrors()) {
                throw new ValidFiledException(ValidationUtils.getValidationErrorString(result), HttpStatus.BAD_REQUEST);
            }
            if (!existingEmail.isPresent()) {
                throw new SendMailException("Your email " + dto.getEmail() + " not found !");
            }
            /*save token random */
            String createToken = StringUtils.randomAlphanumeric(45);
            existingEmail.get().setPasswordResetToken(createToken);
            userRepository.save(existingEmail.get());
            /*create localhost*/
            String link = URLUtil.getSiteURL(request) + "/forgot/reset-password?token=" + createToken;
            /*----*/
            helper.setFrom("contact@shopme.com", "Shopme Support");
            helper.setTo(dto.getEmail());
            String subject = "Here's the link to reset your password";
            String content = "<p>Hello,</p>"
                    + "<p>You have requested to reset your password.</p>"
                    + "<p>Click the link below to change your password:</p>"
                    + "<p><a href=\"" + link + "\">Change my password</a></p>"
                    + "<br>"
                    + "<p>Ignore this email if you do remember your password, "
                    + "or you have not made the request.</p>";
            helper.setSubject(subject);
            helper.setText(content, true);
            javaMailSender.send(message);
            return new CustomResponse("Send Mail Successfully!", HttpStatus.OK.value(), "");
        } catch (Exception e) {
            return new CustomResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "");
        }

    }

    @Override
    public CustomResponse resetPassword(ResetPasswordUser dto, BindingResult result, String token) {
        try {
            Optional<User> existingToken = userRepository.findByPasswordResetToken(token);
            if (!existingToken.isPresent()) {
                throw new ValidFiledException("Param token not found !",HttpStatus.NOT_FOUND);
            }
            if (result.hasErrors()) {
                throw new ValidFiledException(ValidationUtils.getValidationErrorString(result),HttpStatus.BAD_REQUEST);
            }
            existingToken.get().setPassword(passwordEncoder.encode(dto.getPassword()));
            existingToken.get().setPasswordResetToken(null);
            return new CustomResponse("Reset Password successfully!", HttpStatus.OK.value(), userRepository.save(existingToken.get()));
        } catch (CustomException e) {
            return new CustomResponse(e.getMessage(), e.getHttpStatus().value(), "");

        }
    }
}
